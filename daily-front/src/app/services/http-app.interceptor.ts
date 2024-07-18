import {HttpEvent, HttpHandler, HttpInterceptor, HttpInterceptorFn, HttpRequest} from '@angular/common/http';
import {Injectable} from "@angular/core";
import {AppStateService} from "./app-state.service";
import {Observable} from "rxjs";

@Injectable()
export class HttpAppInterceptor implements HttpInterceptor {
  constructor(private appState: AppStateService) {
  }

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    let req = request.clone({
      headers: request.headers.set('Authorization', `Bearer ${this.appState.authState.token}`),
    });
    return next.handle(req);
  }
}
