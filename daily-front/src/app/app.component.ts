import { Component } from '@angular/core';
import {AppStateService} from "./services/app-state.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  constructor(public appState : AppStateService) {
    this.loadAuthState()
  }

  private loadAuthState() {
    const authState = localStorage.getItem('authState');
    if (authState) {
      this.appState.setAuthState(JSON.parse(authState));
    }
  }
}
