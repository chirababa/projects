import { TestBed } from '@angular/core/testing';

import { WebSocketAPIService } from './web-socket-api.service';

describe('WebSocketAPIService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: WebSocketAPIService = TestBed.get(WebSocketAPIService);
    expect(service).toBeTruthy();
  });
});
