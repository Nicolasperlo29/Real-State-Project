import { TestBed } from '@angular/core/testing';

import { PropertyFavoriteService } from './property-favorite.service';

describe('PropertyFavoriteService', () => {
  let service: PropertyFavoriteService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PropertyFavoriteService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
