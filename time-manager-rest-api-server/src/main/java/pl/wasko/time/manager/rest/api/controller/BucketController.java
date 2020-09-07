package pl.wasko.time.manager.rest.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import pl.wasko.time.manager.rest.api.model.response.BucketRestModel;
import pl.wasko.time.manager.rest.api.model.response.CoOwnerRestModel;
import pl.wasko.time.manager.rest.api.model.response.HistoryRestModel;
import pl.wasko.time.manager.rest.api.model.response.UserRestModel;

import pl.wasko.time.manager.rest.api.model.service.BucketService;


import java.util.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RestController
@RequestMapping("/bucket")
@AllArgsConstructor
public class BucketController{

    private final BucketService bucketService;
    
    @GetMapping(value = "/own")
    public ResponseEntity<List<BucketRestModel>> getOwnBuckets(@RequestHeader(value = "Authorization") String token) {
        final List<BucketRestModel> bucketList = bucketService.getOwn(token);
        return ResponseEntity.ok(bucketList);
    }

    @GetMapping(value = "/inactive-user")
    public ResponseEntity<List<BucketRestModel>> getInactiveUserBuckets(@RequestHeader(value = "Authorization") String token,
                                                                        @RequestParam Integer userId) {
        return ResponseEntity.ok(bucketService.getInactiveUserBuckets(userId, token));
    }

    @GetMapping(value = "/shared")
    public ResponseEntity<List<BucketRestModel>> getSharedBuckets(@RequestHeader(value = "Authorization") String token) {
        final List<BucketRestModel> bucketList = bucketService.getShared(token);
        return ResponseEntity.ok(bucketList);
    }

    @GetMapping(value = "/share/{idBucket}")
    public ResponseEntity<List<CoOwnerRestModel>> getBucketShares(@PathVariable Integer idBucket,
                                                                  @RequestHeader(value = "Authorization") String token) {

        return ResponseEntity.ok(bucketService.getBucketShares(idBucket, token));
    }

    @GetMapping(value = "/history/{idBucket}")
    public ResponseEntity<List<HistoryRestModel>> getBucketHistory(@RequestHeader(value = "Authorization") String token, @PathVariable Integer idBucket) {
        final List<HistoryRestModel> historyList = bucketService.getHistory(token, idBucket);
        return ResponseEntity.ok(historyList);
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<BucketRestModel> createBucket(@RequestBody final BucketRestModel bucket,
                                                @RequestHeader(value = "Authorization") String token) {
        return ResponseEntity.ok(bucketService.add(bucket, token));
    }

    @PostMapping(value = "/share/{idBucket}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CoOwnerRestModel>> shareBucket(@RequestBody final List<UserRestModel> userRestModels,
                                                        @PathVariable Integer idBucket,
                                                        @RequestHeader(value = "Authorization") String token) {

        return ResponseEntity.ok( bucketService.share(userRestModels,idBucket, token));

    }

    @PutMapping(value = "/{idBucket}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<BucketRestModel> updateBucket(@RequestBody final BucketRestModel bucketModel,
                                                @PathVariable Integer idBucket,
                                                @RequestHeader(value = "Authorization") String token) {

        return ResponseEntity.ok( bucketService.update(bucketModel,idBucket, token));

    }

    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{idBucket}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer>  deleteBucket(@PathVariable Integer idBucket,
                                                 @RequestHeader(value = "Authorization") String token) {

        bucketService.delete(idBucket,token);
        return new ResponseEntity<>(idBucket, HttpStatus.OK);
    }

    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/share/{idBucket}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer>  deleteBucketCoOwners(@PathVariable Integer idBucket,
                                                         @RequestParam Integer userId,
                                                 @RequestHeader(value = "Authorization") String token) {
        bucketService.deleteCoOwner(idBucket, userId ,token);
        return new ResponseEntity<>(idBucket, HttpStatus.OK);
    }


}
