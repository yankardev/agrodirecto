import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  template: `
    <main class="shell">
      <h1>AgroDirecto</h1>
      <p>Plataforma de comercialización directa del agricultor al consumidor.</p>
      <router-outlet />
    </main>
  `
})
export class AppComponent {}
