package pl.wasko.time.manager.rest.api.model.service;

import org.springframework.stereotype.Service;
import pl.wasko.time.manager.rest.api.exception.NotAuthorizedActionException;
import pl.wasko.time.manager.rest.api.exception.ResourceNotFoundException;
import pl.wasko.time.manager.rest.api.exception.WrongRequestException;
import pl.wasko.time.manager.rest.api.helper.JwtHelper;
import pl.wasko.time.manager.rest.api.model.entity.Bucket;
import pl.wasko.time.manager.rest.api.model.entity.CoOwner;
import pl.wasko.time.manager.rest.api.model.entity.User;
import pl.wasko.time.manager.rest.api.model.enumeration.HistoryElementEnum;
import pl.wasko.time.manager.rest.api.model.enumeration.OperationTypeEnum;
import pl.wasko.time.manager.rest.api.model.enumeration.RoleEnum;
import pl.wasko.time.manager.rest.api.model.repository.BucketRepository;
import pl.wasko.time.manager.rest.api.model.repository.CoOwnerRepository;
import pl.wasko.time.manager.rest.api.model.repository.HistoryRepository;
import pl.wasko.time.manager.rest.api.model.repository.UserRepository;
import pl.wasko.time.manager.rest.api.model.response.BucketRestModel;
import pl.wasko.time.manager.rest.api.model.response.CoOwnerRestModel;
import pl.wasko.time.manager.rest.api.model.response.HistoryRestModel;
import pl.wasko.time.manager.rest.api.model.response.UserRestModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static pl.wasko.time.manager.rest.api.exception.ExceptionMessage.*;


@Service
public class BucketService implements CoOwnerDeleter {

    private final BucketRepository bucketRepository;
    private final UserRepository userRepository;
    private final CoOwnerRepository coOwnerRepository;
    private final JwtHelper jwtHelper;
    private final TaskDeleter taskDeleter;
    private final HistoryService historyService;
    private final HistoryRepository historyRepository;

    public BucketService(BucketRepository bucketRepository, UserRepository userRepository, CoOwnerRepository coOwnerRepository, JwtHelper jwtHelper, TaskDeleter taskDeleter, HistoryService historyService, HistoryRepository historyRepository) {
        this.bucketRepository = bucketRepository;
        this.userRepository = userRepository;
        this.coOwnerRepository = coOwnerRepository;
        this.jwtHelper = jwtHelper;
        this.taskDeleter = taskDeleter;
        this.historyService = historyService;
        this.historyRepository = historyRepository;
    }

    public List<BucketRestModel> getOwn(String token) {
        User currentUser = jwtHelper.getUserFromToken(token);
        return bucketRepository.findAllByUser(currentUser)
                .stream()
                .map(BucketRestModel::new)
                .collect(Collectors.toList());
    }

    public List<BucketRestModel> getShared(String token) {
        User currentUser = jwtHelper.getUserFromToken(token);

        List<Bucket> bucketList = coOwnerRepository.findAllByUserAndDeletedFalse(currentUser)
                .stream()
                .map(CoOwner::getBucket)
                .collect(Collectors.toList());

        return bucketList.stream()
                .map(BucketRestModel::new)
                .collect(Collectors.toList());
    }

    public List<BucketRestModel> getInactiveUserBuckets(Integer userId, String token) {
        User currentUser = jwtHelper.getUserFromToken(token);
        User user = userRepository.findById(userId).orElseThrow(() ->
            new ResourceNotFoundException(USER_NOT_FOUND.getMessage())
        );

        if(!currentUser.getRole().getRoleName().equals(RoleEnum.ADMIN) || user.getActive().equals(true))
            throw new NotAuthorizedActionException(CANNOT_GET_INACTIVE_USER_BUCKETS.getMessage());

        return user.getListOfBuckets().stream().map(BucketRestModel::new).collect(Collectors.toList());
    }

    public List<HistoryRestModel> getHistory(String token, Integer idBucket) {

        Bucket bucket = bucketRepository.findById(idBucket).orElseThrow(() ->
                new ResourceNotFoundException(BUCKET_NOT_FOUND.getMessage())
        );

        User currentUser = jwtHelper.getUserFromToken(token);

        if(bucket.isUserOwner(currentUser))
          return historyService.getFromHistory(bucket, HistoryElementEnum.BUCKET);
        else
            throw new NotAuthorizedActionException(NOT_AUTHORIZED_TO_BUCKET.getMessage());

    }

    public BucketRestModel add(final BucketRestModel bucket, final String token) {

        User currentUser = jwtHelper.getUserFromToken(token);
        Bucket currentBucket;

        if (checkAmountOfBuckets(currentUser))
        {
            currentBucket = new Bucket(bucket.getBucketName(), bucket.getDescription(), bucket.setAndReturnCreationDate(new Date()), currentUser, bucket.getMaxTasks(), false);
        }
        else
            throw new WrongRequestException(CANNOT_ADD_BUCKETS.getMessage());

        BucketRestModel actualBucket = new BucketRestModel(bucketRepository.save(currentBucket));
        historyService.saveToHistory(currentBucket, currentUser, OperationTypeEnum.CREATE);

        return actualBucket;

    }

    public Boolean checkAmountOfBuckets(User currentUser) {

        int amountOfBuckets = currentUser.getListOfBuckets().size();
        int maxAmountOfBuckets = currentUser.getRole().getMaxBuckets();

        if (maxAmountOfBuckets > amountOfBuckets)
            return true;
        else
            return false;
    }

    public void delete(Integer idBucket, String token) {

        Bucket currentBucket = bucketRepository.findById(idBucket).orElseThrow(() ->
                new ResourceNotFoundException(BUCKET_NOT_FOUND.getMessage()));

        User currentUser = jwtHelper.getUserFromToken(token);

        if (currentBucket.isUserOwner(currentUser)) {

            bucketRepository.findById(idBucket)
                    .map(bucket -> {
                        bucket.setDeleted(true);
                        return bucketRepository.save(bucket);
                    });

            List<CoOwner> coOwnersList = coOwnerRepository.findAllByBucket(currentBucket);
            List<CoOwner> editedCoOwnersList = new ArrayList<>();

            for (CoOwner coOwner : coOwnersList) {
                coOwner.setDeleted(true);
                editedCoOwnersList.add(coOwner);
            }

            coOwnerRepository.saveAll(editedCoOwnersList);
            taskDeleter.deleteTasks(currentBucket.getListOfTasks(),currentUser);

        } else
            throw new NotAuthorizedActionException(NOT_ALLOWED_DELETE_BUCKET.getMessage());

        historyService.saveToHistory(currentBucket,currentUser, OperationTypeEnum.DELETE);
    }

    public void deleteCoOwner(Integer idBucket, Integer idUser, String token) {

        Bucket currentBucket = bucketRepository.findById(idBucket).orElseThrow(() ->
                new ResourceNotFoundException(BUCKET_NOT_FOUND.getMessage()));

        User currentUser = jwtHelper.getUserFromToken(token);
        if (currentBucket.isUserOwner(currentUser)) {

            CoOwner coOwner = currentBucket.getListOfCoOwners().stream()
                    .filter(co -> co.getUser().getIdUser().equals(idUser)).findFirst().orElseThrow(() ->
                            new WrongRequestException(COOWNER_NOT_FOUND.getMessage()));

            taskDeleter.onBucketCoOwnerDeleted(coOwner);
            coOwner.setDeleted(true);
            coOwnerRepository.save(coOwner);

        } else
            throw new NotAuthorizedActionException(NOT_ALLOWED_DELETE_BUCKET.getMessage());

    }

    public BucketRestModel update(BucketRestModel bucketModel, Integer idBucket, String token) {

        Bucket currentBucket = bucketRepository.findById(idBucket).orElseThrow(() ->
                new ResourceNotFoundException(BUCKET_NOT_FOUND.getMessage()));

        User currentUser = jwtHelper.getUserFromToken(token);

        if (currentBucket.isUserOwner(currentUser)) {

            if(checkAmountOfTasks(currentBucket, bucketModel)) {

                currentBucket.setBucketName(bucketModel.getBucketName());
                currentBucket.setDescription(bucketModel.getDescription());
                currentBucket.setMaxTasks(bucketModel.getMaxTasks());

            } else
                throw new WrongRequestException(WRONG_UPDATE_TASK_NUMBER.getMessage());

        } else if (currentBucket.isUserCoOwner(currentUser)) {

            currentBucket.setBucketName(bucketModel.getBucketName());
            currentBucket.setDescription(bucketModel.getDescription());

        } else
            throw new NotAuthorizedActionException(NOT_ALLOWED_UPDATE_BUCKET.getMessage());

        BucketRestModel actualBucket = new BucketRestModel(bucketRepository.save(currentBucket));
        historyService.saveToHistory(currentBucket,currentUser, OperationTypeEnum.UPDATE);
        return actualBucket;

    }

    public Boolean checkAmountOfTasks(Bucket currentBucket, BucketRestModel bucketModel) {

        int givenMaxTasks = bucketModel.getMaxTasks();
        int currentTasks = currentBucket.getListOfTasks().size();

        if (givenMaxTasks >= currentTasks)
            return true;
        else
            return false;
    }

    public List<CoOwnerRestModel> share(List<UserRestModel> userRestModels, Integer idBucket, String token) {

        Bucket currentBucket = bucketRepository.findById(idBucket).orElseThrow(() ->
                new ResourceNotFoundException(BUCKET_NOT_FOUND.getMessage()));

        User currentUser = jwtHelper.getUserFromToken(token);

        if (currentBucket.isUserOwner(currentUser)) {
            Set<Integer> ids = userRestModels.stream().map(UserRestModel::getIdUser).collect(Collectors.toSet());
            if(ids.contains(currentUser.getIdUser()))
                throw new WrongRequestException(CANNOT_SHARE_BUCKET_TO_YOURSELF.getMessage());

            List<User> newCoOwners = userRepository.findAllByIdUserIsIn(ids);

            if (newCoOwners.size() != ids.size())
                throw new WrongRequestException(WRONG_USER_IDS.getMessage());

            List<CoOwner> bucketCoOwners = currentBucket.getListOfCoOwners();

            List<CoOwner> listOfNewCoOwners = bucketCoOwners.stream()
                    .filter(it ->
                            it.getDeleted().equals(true) &&
                                    newCoOwners.stream()
                                            .anyMatch(it2 -> it.getUser().equals(it2))
                    )
                    .peek(it -> it.setDeleted(false)).collect(Collectors.toList());

            newCoOwners.stream().filter(it -> bucketCoOwners.stream().noneMatch(it2 -> it2.getUser().equals(it)))
                    .forEach(newCoOwner -> {
                        CoOwner coOwner = new CoOwner();
                        coOwner.setDeleted(false);
                        coOwner.setUser(newCoOwner);
                        coOwner.setBucket(currentBucket);
                        listOfNewCoOwners.add(coOwner);
                    });


            coOwnerRepository.saveAll(listOfNewCoOwners);

            return listOfNewCoOwners.stream()
                    .map(CoOwnerRestModel::new)
                    .collect(Collectors.toList());

        } else
            throw new NotAuthorizedActionException(NOT_ALLOWED_SHARE_BUCKET.getMessage());

    }

    public List<CoOwnerRestModel> getBucketShares(Integer idBucket, String token) {
        Bucket currentBucket = bucketRepository.findById(idBucket).orElseThrow(() ->
                new ResourceNotFoundException(BUCKET_NOT_FOUND.getMessage()));

        User currentUser = jwtHelper.getUserFromToken(token);

        if (!currentBucket.isUserOwner(currentUser))
            throw new NotAuthorizedActionException(NOT_ALLOWED_GET_BUCKET_SHARES.getMessage());
        
        return currentBucket.getListOfCoOwners().stream()
                .filter(it -> it.getDeleted().equals(false))
                .map(CoOwnerRestModel::new).collect(Collectors.toList());
    }

    @Override
    public void deleteUserCoOwners(User user) {
        List<CoOwner> coOwners = user.getListOfCoOwners().stream().peek(coOwner -> {
            coOwner.setDeleted(true);
            taskDeleter.onBucketCoOwnerDeleted(coOwner);
        }).collect(Collectors.toList());

        coOwnerRepository.saveAll(coOwners);
    }
}
