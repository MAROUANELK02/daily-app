import {ActivatedRouteSnapshot, CanActivateFn, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import {Injectable} from "@angular/core";
import {AppStateService} from "../services/app-state.service";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class UserAuthorizationGuard {
  constructor(private appState : AppStateService, private router : Router) {
  }
  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    let authorized=false;
    if(!this.appState.authState.isAuthenticated){
      authorized=false;
    } else if (this.appState.authState.roles.includes("ROLE_USER")) {
      authorized=true;
    }
    if(authorized) {
      return true;
    } else {
      this.router.navigateByUrl("/notAuthorized");
      return false;
    }
  }
}
