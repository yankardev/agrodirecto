import { Component, computed, inject, signal } from '@angular/core';
import { CurrencyPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { DemoStore } from './demo-store.service';

type ShopView = 'catalogo' | 'pedidos' | 'reclamos';

@Component({
  selector: 'app-storefront',
  standalone: true,
  imports: [CurrencyPipe, FormsModule, RouterLink],
  template: `
    <div class="shop-page">
      <div class="announcement">🌾 Compra directamente a productores locales · Frescura que nos conecta</div>
      <header class="shop-header">
        <div class="container shop-header-inner">
          <a class="brand" routerLink="/"><span class="brand-icon">🌱</span><span>Agro<span class="brand-accent">Directo</span><small>Del agricultor a tu mesa</small></span></a>
          <nav class="shop-nav" aria-label="Navegación de compras">
            <button [class.active]="view() === 'catalogo'" (click)="view.set('catalogo')">Inicio y catálogo</button>
            <button [class.active]="view() === 'pedidos'" (click)="view.set('pedidos')">Mis pedidos</button>
            <button [class.active]="view() === 'reclamos'" (click)="view.set('reclamos')">Libro de reclamaciones</button>
          </nav>
          <div class="header-actions">
            <button class="cart-counter" (click)="view.set('catalogo')" aria-label="Ver carrito">🛒 <span>{{ store.cartCount() }}</span></button>
            <a class="role-switch" routerLink="/admin">Panel admin ↗</a>
          </div>
        </div>
      </header>
      <main class="container shop-main">
        @if (view() === 'catalogo') {
          <section class="shop-hero">
            <div class="hero-copy">
              <span class="hero-tag">🌿 DIRECTO DEL CAMPO</span>
              <h1>Lo mejor de nuestra tierra, <em>hasta tu mesa.</em></h1>
              <p>Frutas, verduras y productos frescos de agricultores peruanos. Compra local, compra con propósito.</p>
              <a href="#productos" class="btn btn-light">Explorar productos <span>→</span></a>
            </div>
            <div class="hero-produce" aria-hidden="true"><span class="produce-1">🥑</span><span class="produce-2">🥕</span><span class="produce-3">🥬</span><span class="produce-4">🍅</span><span class="produce-5">🌽</span></div>
          </section>
          <div class="benefits-strip"><span>🚜 <strong>Productores locales</strong></span><span>🥬 <strong>Frescura garantizada*</strong></span><span>🤝 <strong>Compra sin intermediarios</strong></span></div>
          <section id="productos" class="catalog-section">
            <div class="section-heading"><div><span class="eyebrow">NUESTRO MERCADO</span><h2>Productos del campo</h2><p>Selecciona lo que necesitas y agrégalo a tu carrito.</p></div><span class="item-count">{{ filteredProducts().length }} productos</span></div>
            <div class="catalog-toolbar">
              <div class="category-tabs">
                @for (category of categories; track category) {
                  <button [class.selected]="selectedCategory() === category" (click)="selectedCategory.set(category)">{{ category }}</button>
                }
              </div>
              <label class="search-field"><span>⌕</span><input type="search" placeholder="Buscar productos..." [ngModel]="search()" (ngModelChange)="search.set($event)" aria-label="Buscar productos"></label>
            </div>
            <div class="shopping-layout">
              <div class="product-grid">
                @for (product of filteredProducts(); track product.id) {
                  <article class="product-card">
                    <div class="product-visual" [class]="'product-visual tint-' + product.tint">
                      <span class="organic-badge">Producto local</span><span class="product-emoji">{{ product.emoji }}</span>
                    </div>
                    <div class="product-info">
                      <span class="product-category">{{ product.category }}</span>
                      <h3>{{ product.name }}</h3>
                      <p class="farmer-name">🌱 {{ product.farmer }}</p>
                      <div class="product-bottom"><div class="price">{{ product.price | currency:'PEN':'symbol':'1.2-2' }} <small>/ {{ product.unit }}</small></div><span class="stock" [class.low]="product.stock < 10">{{ product.stock }} disp.</span></div>
                      <button class="btn add-button" [disabled]="product.stock === 0" (click)="add(product.id)">{{ product.stock === 0 ? 'Agotado' : '+ Agregar al carrito' }}</button>
                    </div>
                  </article>
                } @empty {
                  <div class="empty-state">No encontramos productos con esa búsqueda.</div>
                }
              </div>
              <aside class="cart-panel">
                <div class="cart-heading"><div><h3>🛒 Tu carrito</h3><p>{{ store.cartCount() }} productos seleccionados</p></div><span class="cart-bubble">{{ store.cartCount() }}</span></div>
                @if (store.cartLines().length === 0) {
                  <div class="empty-cart"><div>🧺</div><strong>Tu carrito está vacío</strong><p>Agrega productos frescos para empezar.</p></div>
                } @else {
                  <div class="cart-lines">
                    @for (line of store.cartLines(); track line.product.id) {
                      <div class="cart-line"><div class="cart-line-icon" [class]="'cart-line-icon tint-' + line.product.tint">{{ line.product.emoji }}</div><div class="cart-line-details"><strong>{{ line.product.name }}</strong><small>{{ line.product.price | currency:'PEN':'symbol':'1.2-2' }} / {{ line.product.unit }}</small><div class="qty-controls"><button (click)="store.changeQuantity(line.product.id, -1)" aria-label="Quitar uno">−</button><span>{{ line.quantity }}</span><button (click)="store.changeQuantity(line.product.id, 1)" aria-label="Agregar uno">+</button></div></div><div class="cart-line-end"><button class="remove-button" (click)="store.removeFromCart(line.product.id)" aria-label="Eliminar producto">×</button><strong>{{ line.subtotal | currency:'PEN':'symbol':'1.2-2' }}</strong></div></div>
                    }
                  </div>
                  <div class="cart-checkout">
                    <div class="total-line"><span>Subtotal</span><strong>{{ store.cartTotal() | currency:'PEN':'symbol':'1.2-2' }}</strong></div>
                    <p class="checkout-hint">Demostración sin pagos ni envío real.</p>
                    <label>Nombre del comprador<input [(ngModel)]="customer" placeholder="Tu nombre completo"></label>
                    <label>Dirección de referencia<input [(ngModel)]="address" placeholder="Distrito o dirección"></label>
                    <button class="btn btn-primary full-width" (click)="checkout()">Confirmar pedido →</button>
                  </div>
                }
                @if (message()) { <p class="feedback" role="status">{{ message() }}</p> }
              </aside>
            </div>
          </section>
        } @else if (view() === 'pedidos') {
          <section class="subpage-title"><span class="eyebrow">SEGUIMIENTO DE COMPRAS</span><h1>Mis pedidos</h1><p>Pedidos registrados en esta demostración local.</p></section>
          <div class="orders-list">
            @for (order of store.orders(); track order.id) {
              <article class="order-card"><div class="order-top"><div><span class="order-number">Pedido #{{ order.id }}</span><small>{{ order.date }} · {{ order.customer }}</small></div><span class="status-pill" [class.delivered]="order.status === 'Entregado'">{{ order.status }}</span></div><div class="order-content"><span>{{ order.lines.length }} tipo(s) de producto · {{ order.address }}</span><strong>{{ order.total | currency:'PEN':'symbol':'1.2-2' }}</strong></div><p class="order-detail">@for (line of order.lines; track $index) { {{ line.quantity }} × {{ line.name }} · {{ line.farmer }} &nbsp; }</p></article>
            } @empty { <div class="empty-state">Aún no hay pedidos.</div> }
          </div>
        } @else {
          <section class="subpage-title"><span class="eyebrow">ATENCIÓN AL CLIENTE</span><h1>Libro de reclamaciones</h1><p>Registra un comentario o reclamo para esta demostración.</p></section>
          <div class="complaint-panel"><label>Nombre completo<input [(ngModel)]="complaintName" placeholder="Ingresa tu nombre"></label><label>Detalle del reclamo<textarea [(ngModel)]="complaintText" rows="5" placeholder="Describe tu consulta o reclamo"></textarea></label><button class="btn btn-primary" (click)="submitComplaint()">Registrar reclamo</button>@if (message()) { <p class="feedback" role="status">{{ message() }}</p> }<p class="checkout-hint">Los registros se guardan únicamente en este navegador; no constituyen un libro oficial de reclamaciones.</p></div>
        }
      </main>
      <footer class="site-footer"><div class="container"><strong>🌱 AgroDirecto</strong><span>Proyecto académico · Interfaz de compras · Datos de demostración</span><a routerLink="/">Cambiar de interfaz ↑</a></div></footer>
    </div>
  `
})
export class StorefrontComponent {
  readonly store = inject(DemoStore);
  readonly view = signal<ShopView>('catalogo');
  readonly selectedCategory = signal('Todos');
  readonly search = signal('');
  readonly message = signal('');
  readonly categories = ['Todos', 'Frutas', 'Verduras', 'Tubérculos', 'Granos'];
  readonly filteredProducts = computed(() => this.store.products().filter(product => {
    const matchesCategory = this.selectedCategory() === 'Todos' || product.category === this.selectedCategory();
    const query = this.search().trim().toLocaleLowerCase();
    return matchesCategory && (!query || (product.name + ' ' + product.farmer).toLocaleLowerCase().includes(query));
  }));
  customer = '';
  address = '';
  complaintName = '';
  complaintText = '';

  add(id: number): void {
    this.store.changeQuantity(id, 1);
    this.message.set('Producto agregado al carrito.');
  }
  checkout(): void {
    const result = this.store.checkout(this.customer, this.address);
    this.message.set(result);
    if (result.startsWith('Pedido #')) { this.customer = ''; this.address = ''; this.view.set('pedidos'); }
  }
  submitComplaint(): void {
    const ok = this.store.addComplaint(this.complaintName, this.complaintText);
    this.message.set(ok ? 'Tu registro se guardó en esta demostración.' : 'Completa tu nombre y el detalle.');
    if (ok) { this.complaintName = ''; this.complaintText = ''; }
  }
}
