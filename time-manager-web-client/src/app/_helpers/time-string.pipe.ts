import { Pipe, PipeTransform } from '@angular/core';

export enum TransformDirection {
  toString,
  toSeconds
}

@Pipe({name: 'timeString'})
export class TimeStringPipe implements PipeTransform {
  transform(value: any, direction: TransformDirection = TransformDirection.toString): any {
    if (direction === TransformDirection.toString) {
      const seconds = value;
      let result = '';
      let n: number;
      n = Math.floor(seconds / 3600);
      result += n < 10 ? ('0' + n.toString()) : n;
      result += ':';

      n = Math.floor((seconds % 3600) / 60);
      result += n < 10 ? ('0' + n.toString()) : n;
      result += ':';

      n = Math.floor(seconds % 60);
      result += n < 10 ? ('0' + n.toString()) : n;

      return result;
    } else {
      let result: number;
      const split: string[] = value.split(':');
      result = +split[0] * 3600 + +split[1] * 60 + +split[2];
      return result;
    }
  }
}
