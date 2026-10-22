import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-access',
  standalone: true,
  imports: [RouterLink],
  template: `
    <div class="access-page">
      <header class="access-header container">
        <a class="brand" routerLink="/"><span class="brand-icon">🌱</span><span>Agro<span class="brand-accent">Directo</span><small>Del agricultor a tu mesa</small></span></a>
        <span class="demo-pill">● Prototipo local</span>
      </header>
      <main class="access-main container">
        <section class="access-intro">
          <span class="eyebrow">MERCADO AGRÍCOLA DIGITAL</span>
          <h1>Productos frescos, <em>conexiones reales.</em></h1>
          <p>Una plataforma que acerca a pequeños agricultores y consumidores, con un espacio de compras y un panel de gestión diferenciados.</p>
          <div class="access-features">
            <span>✓ Compra directa</span><span>✓ Productos locales</span><span>✓ Gestión de pedidos</span>
          </div>
          <div class="produce-art" aria-hidden="true"><span>🥬</span><span>🥕</span><span>🥑</span><span>🍅</span></div>
        </section>
        <section class="access-panel">
          <div class="panel-kicker">BIENVENIDO A AGRODIRECTO</div>
          <h2>¿Cómo deseas ingresar?</h2>
          <p>Elige una interfaz para explorar el prototipo.</p>
          <a class="access-option" routerLink="/tienda">
            <span class="option-icon customer">🛒</span>
            <span><strong>Área de compras</strong><small>Catálogo, carrito y mis pedidos</small></span>
            <span class="option-arrow">→</span>
          </a>
          <a class="access-option" routerLink="/admin">
            <span class="option-icon manager">📊</span>
            <span><strong>Área administrativa</strong><small>Inventario, pedidos y reportes</small></span>
            <span class="option-arrow">→</span>
          </a>
          <div class="access-note">🔒 <span>Acceso de demostración. Aún no implementa inicio de sesión ni permisos reales.</span></div>
        </section>
      </main>
      <footer class="access-footer">AgroDirecto · Proyecto académico EFSRT · Demostración de interfaz</footer>
    </div>
  `
})
export class AccessComponent {}
