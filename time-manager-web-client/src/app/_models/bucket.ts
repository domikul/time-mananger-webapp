import {Shareable} from './shareable';

export class Bucket implements Shareable {

  idBucket: number;
  bucketName: string;
  description: string;
  creationDate: Date;
  userId: number;
  maxTasks: number;

  public static makeCopy(bucket: Bucket): Bucket {
    const copied = new Bucket();

    copied.idBucket = bucket.idBucket;
    copied.bucketName = bucket.bucketName;
    copied.description = bucket.description;
    copied.creationDate = bucket.creationDate;
    copied.userId = bucket.userId;
    copied.maxTasks = bucket.maxTasks;

    return copied;
  }

  getShareableId() {
    return this.idBucket;
  }

  getShareableName() {
    return this.bucketName;
  }

}
