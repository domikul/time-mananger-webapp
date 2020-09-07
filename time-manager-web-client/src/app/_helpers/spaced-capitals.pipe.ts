import { Pipe, PipeTransform } from '@angular/core';

@Pipe({name: 'spacedCapitals'})
export class SpacedCapitalsPipe implements PipeTransform {
  transform(value: string): string {
    return value ? value.replace(/_/g, ' ').toUpperCase() : value;
  }
}
