import { Component, computed, inject, signal } from '@angular/core';
import { CurrencyPipe, DecimalPipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { DemoStore, OrderStatus } from './demo-store.service';

type AdminSection = 'resumen' | 'productos' | 'pedidos' | 'agricultores' | 'reportes' | 'reclamos';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [CurrencyPipe, DecimalPipe, RouterLink],
  template: `
    <div class="admin-layout">
      <aside class="admin-sidebar">
        <a class="brand admin-brand" routerLink="/"><span class="brand-icon">🌱</span><span>Agro<span class="brand-accent">Directo</span><small>Panel administrativo</small></span></a>
        <div class="sidebar-label">MENÚ PRINCIPAL</div>
        <nav class="admin-nav" aria-label="Administración">
          <button [class.current]="section() === 'resumen'" (click)="section.set('resumen')"><span>▦</span> Resumen general</button>
          <button [class.current]="section() === 'productos'" (click)="section.set('productos')"><span>🥬</span> Productos e inventario</button>
          <button [class.current]="section() === 'pedidos'" (click)="section.set('pedidos')"><span>📦</span> Gestión de pedidos</button>
          <button [class.current]="section() === 'agricultores'" (click)="section.set('agricultores')"><span>🚜</span> Agricultores</button>
          <button [class.current]="section() === 'reportes'" (click)="section.set('reportes')"><span>📈</span> Reportes</button>
          <button [class.current]="section() === 'reclamos'" (click)="section.set('reclamos')"><span>💬</span> Reclamaciones</button>
        </nav>
        <div class="sidebar-bottom"><div class="demo-admin-note">● Modo demostración<br><small>Sin control de acceso real</small></div><a routerLink="/tienda">↗ Ir a la tienda</a><a routerLink="/">← Cambiar interfaz</a></div>
      </aside>
      <div class="admin-workspace">
        <header class="admin-topbar"><div class="admin-breadcrumb">AgroDirecto <span>/</span> Administración</div><div class="admin-user"><span class="online-dot"></span> Demo administrador <span class="avatar">AD</span></div></header>
        <main class="admin-content">
          <div class="admin-page-heading"><div><span class="eyebrow">PANEL DE CONTROL</span><h1>{{ heading() }}</h1><p>{{ subtitle() }}</p></div><span class="date-label">📅 Datos locales de demostración</span></div>
          @if (section() === 'resumen') {
            <div class="stat-grid">
              <article class="stat-card"><span class="stat-icon green">💰</span><span>Ventas registradas</span><strong>{{ revenue() | currency:'PEN':'symbol':'1.2-2' }}</strong><small>Pedidos de demostración</small></article>
              <article class="stat-card"><span class="stat-icon orange">📦</span><span>Total de pedidos</span><strong>{{ store.orders().length }}</strong><small>{{ pendingOrders() }} pendientes</small></article>
              <article class="stat-card"><span class="stat-icon blue">🥬</span><span>Productos activos</span><strong>{{ activeProducts() }}</strong><small>{{ store.products().length }} en catálogo</small></article>
              <article class="stat-card"><span class="stat-icon violet">🚜</span><span>Agricultores aprobados</span><strong>{{ approvedFarmers() }}</strong><small>{{ store.farmers().length }} registrados</small></article>
            </div>
            <div class="admin-columns">
              <section class="admin-card"><div class="card-title-row"><div><h2>Últimos pedidos</h2><p>Actividad reciente de la tienda</p></div><button class="text-link" (click)="section.set('pedidos')">Ver todos →</button></div><div class="table-scroll"><table class="data-table"><thead><tr><th>Pedido</th><th>Cliente</th><th>Total</th><th>Estado</th></tr></thead><tbody>@for (order of store.orders().slice(0, 5); track order.id) { <tr><td class="emphasis">#{{ order.id }}</td><td>{{ order.customer }}</td><td>{{ order.total | currency:'PEN':'symbol':'1.2-2' }}</td><td><span class="status-pill" [class.delivered]="order.status === 'Entregado'">{{ order.status }}</span></td></tr> }</tbody></table></div></section>
              <section class="admin-card"><div class="card-title-row"><div><h2>Inventario en alerta</h2><p>Productos con menos de 30 unidades</p></div></div><div class="low-stock-list">@for (product of lowStockProducts(); track product.id) { <div class="low-stock-item"><span class="tiny-product" [class]="'tiny-product tint-' + product.tint">{{ product.emoji }}</span><div><strong>{{ product.name }}</strong><small>{{ product.farmer }}</small></div><span class="stock-warning">{{ product.stock }} uds.</span></div> } @empty { <p>Sin alertas de inventario.</p> }</div></section>
            </div>
          } @else if (section() === 'productos') {
            <section class="admin-card"><div class="card-title-row"><div><h2>Inventario de productos</h2><p>Modifica las existencias para probar la interfaz.</p></div><span class="item-count">{{ store.products().length }} productos</span></div><div class="table-scroll"><table class="data-table"><thead><tr><th>Producto</th><th>Agricultor</th><th>Categoría</th><th>Precio</th><th>Stock</th><th>Ajustar</th></tr></thead><tbody>@for (product of store.products(); track product.id) { <tr><td><div class="table-product"><span class="tiny-product" [class]="'tiny-product tint-' + product.tint">{{ product.emoji }}</span><strong>{{ product.name }}</strong></div></td><td>{{ product.farmer }}</td><td>{{ product.category }}</td><td>{{ product.price | currency:'PEN':'symbol':'1.2-2' }}</td><td><span [class.stock-warning]="product.stock < 10">{{ product.stock }} {{ product.unit }}</span></td><td><div class="qty-controls"><button (click)="store.updateStock(product.id, -1)" aria-label="Reducir stock">−</button><span>{{ product.stock }}</span><button (click)="store.updateStock(product.id, 1)" aria-label="Aumentar stock">+</button></div></td></tr> }</tbody></table></div></section>
          } @else if (section() === 'pedidos') {
            <section class="admin-card"><div class="card-title-row"><div><h2>Administración de pedidos</h2><p>Actualiza el estado de los pedidos registrados.</p></div><span class="item-count">{{ store.orders().length }} pedidos</span></div><div class="table-scroll"><table class="data-table"><thead><tr><th>Pedido</th><th>Comprador</th><th>Fecha</th><th>Total</th><th>Estado</th><th>Cambiar estado</th></tr></thead><tbody>@for (order of store.orders(); track order.id) { <tr><td class="emphasis">#{{ order.id }}</td><td>{{ order.customer }}</td><td>{{ order.date }}</td><td>{{ order.total | currency:'PEN':'symbol':'1.2-2' }}</td><td><span class="status-pill" [class.delivered]="order.status === 'Entregado'">{{ order.status }}</span></td><td><select class="status-select" [value]="order.status" (change)="changeStatus(order.id, $event)">@for (status of statuses; track status) { <option [value]="status">{{ status }}</option> }</select></td></tr> }</tbody></table></div></section>
          } @else if (section() === 'agricultores') {
            <section class="admin-card"><div class="card-title-row"><div><h2>Productores registrados</h2><p>Aprueba o desactiva productores en esta demostración.</p></div></div><div class="farmer-list">@for (farmer of store.farmers(); track farmer.id) { <div class="farmer-row"><span class="farmer-avatar">🚜</span><div class="farmer-details"><strong>{{ farmer.name }}</strong><small>📍 {{ farmer.location }}</small></div><span class="status-pill" [class.delivered]="farmer.approved">{{ farmer.approved ? 'Aprobado' : 'Pendiente' }}</span><button class="btn btn-outline" (click)="store.toggleFarmer(farmer.id)">{{ farmer.approved ? 'Desactivar' : 'Aprobar' }}</button></div> }</div></section>
          } @else if (section() === 'reportes') {
            <div class="stat-grid report-stats"><article class="stat-card"><span>Total vendido</span><strong>{{ revenue() | currency:'PEN':'symbol':'1.2-2' }}</strong></article><article class="stat-card"><span>Ticket promedio</span><strong>{{ averageTicket() | currency:'PEN':'symbol':'1.2-2' }}</strong></article><article class="stat-card"><span>Pedidos entregados</span><strong>{{ deliveredOrders() }}</strong></article></div>
            <section class="admin-card"><div class="card-title-row"><div><h2>Ventas por producto</h2><p>Visualización de los pedidos guardados en el navegador.</p></div><span class="demo-pill">Vista preliminar</span></div><div class="report-bars">@for (item of productSales(); track item.name) { <div class="report-bar-row"><span>{{ item.name }}</span><div class="bar-track"><div class="bar-fill" [style.width.%]="maxSale() ? item.total / maxSale() * 100 : 0"></div></div><strong>{{ item.total | currency:'PEN':'symbol':'1.2-2' }}</strong></div> }</div><p class="checkout-hint">La exportación PDF/Excel/CSV y los reportes del backend se implementarán en una etapa posterior.</p></section>
          } @else {
            <section class="admin-card"><div class="card-title-row"><div><h2>Registros de reclamaciones</h2><p>Enviados desde la interfaz de compras en este navegador.</p></div></div>@for (complaint of store.complaints(); track complaint.id) { <div class="complaint-row"><div><strong>{{ complaint.customer }}</strong><small>{{ complaint.date }}</small></div><p>{{ complaint.message }}</p></div> } @empty { <div class="empty-state">Todavía no hay reclamaciones registradas.</div> }</section>
          }
          <div class="admin-footer"><span>AgroDirecto · Panel de demostración</span><button (click)="resetDemo()">↺ Restablecer datos de prueba</button></div>
        </main>
      </div>
    </div>
  `
})
export class AdminComponent {
  readonly store = inject(DemoStore);
  readonly section = signal<AdminSection>('resumen');
  readonly statuses: OrderStatus[] = ['Pendiente', 'En preparación', 'En camino', 'Entregado'];
  readonly revenue = computed(() => this.store.orders().reduce((sum, order) => sum + order.total, 0));
  readonly pendingOrders = computed(() => this.store.orders().filter(order => order.status !== 'Entregado').length);
  readonly deliveredOrders = computed(() => this.store.orders().filter(order => order.status === 'Entregado').length);
  readonly activeProducts = computed(() => this.store.products().filter(product => product.stock > 0).length);
  readonly approvedFarmers = computed(() => this.store.farmers().filter(farmer => farmer.approved).length);
  readonly averageTicket = computed(() => this.store.orders().length ? this.revenue() / this.store.orders().length : 0);
  readonly lowStockProducts = computed(() => this.store.products().filter(product => product.stock < 30));
  readonly productSales = computed(() => {
    const sales = new Map<string, number>();
    for (const order of this.store.orders()) for (const line of order.lines) sales.set(line.name, (sales.get(line.name) ?? 0) + line.price * line.quantity);
    return [...sales.entries()].map(([name, total]) => ({ name, total })).sort((a, b) => b.total - a.total);
  });
  readonly maxSale = computed(() => Math.max(0, ...this.productSales().map(item => item.total)));
  readonly heading = computed(() => ({
    resumen: 'Resumen general', productos: 'Productos e inventario', pedidos: 'Gestión de pedidos',
    agricultores: 'Gestión de agricultores', reportes: 'Reportes de ventas', reclamos: 'Reclamaciones'
  })[this.section()]);
  readonly subtitle = computed(() => ({
    resumen: 'Monitorea la actividad de AgroDirecto desde un solo lugar.',
    productos: 'Consulta y ajusta la disponibilidad del catálogo.',
    pedidos: 'Revisa compras y administra su seguimiento.',
    agricultores: 'Gestiona los productores de la plataforma.',
    reportes: 'Indicadores calculados a partir de los pedidos de prueba.',
    reclamos: 'Consulta los mensajes registrados por compradores.'
  })[this.section()]);

  changeStatus(orderId: number, event: Event): void {
    const value = (event.target as HTMLSelectElement).value as OrderStatus;
    if (this.statuses.includes(value)) this.store.updateOrderStatus(orderId, value);
  }
  resetDemo(): void {
    if (confirm('¿Restablecer todos los datos de demostración en este navegador?')) this.store.reset();
  }
}
