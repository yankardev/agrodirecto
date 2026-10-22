import { Injectable, computed, signal } from '@angular/core';

export interface Product {
  id: number;
  name: string;
  category: string;
  farmer: string;
  price: number;
  stock: number;
  unit: string;
  emoji: string;
  tint: string;
}

export interface CartItem { productId: number; quantity: number; }
export interface OrderLine { name: string; farmer: string; quantity: number; price: number; }
export type OrderStatus = 'Pendiente' | 'En preparación' | 'En camino' | 'Entregado';
export interface Order {
  id: number;
  customer: string;
  address: string;
  date: string;
  lines: OrderLine[];
  total: number;
  status: OrderStatus;
}
export interface Farmer { id: number; name: string; location: string; approved: boolean; }
export interface Complaint { id: number; customer: string; message: string; date: string; }

const INITIAL_PRODUCTS: Product[] = [
  { id: 1, name: 'Palta Hass', category: 'Frutas', farmer: 'Fundo Los Olivos', price: 9.50, stock: 36, unit: 'kg', emoji: '🥑', tint: 'mint' },
  { id: 2, name: 'Tomate fresco', category: 'Verduras', farmer: 'Huerto Santa Rosa', price: 4.20, stock: 58, unit: 'kg', emoji: '🍅', tint: 'rose' },
  { id: 3, name: 'Papa amarilla', category: 'Tubérculos', farmer: 'Asociación Valle Verde', price: 5.80, stock: 80, unit: 'kg', emoji: '🥔', tint: 'sand' },
  { id: 4, name: 'Zanahoria', category: 'Verduras', farmer: 'Huerto Santa Rosa', price: 3.50, stock: 64, unit: 'kg', emoji: '🥕', tint: 'peach' },
  { id: 5, name: 'Mango Kent', category: 'Frutas', farmer: 'Fundo Los Olivos', price: 7.90, stock: 28, unit: 'kg', emoji: '🥭', tint: 'sun' },
  { id: 6, name: 'Lechuga orgánica', category: 'Verduras', farmer: 'Granja La Esperanza', price: 3.00, stock: 42, unit: 'unidad', emoji: '🥬', tint: 'mint' },
  { id: 7, name: 'Maíz choclo', category: 'Granos', farmer: 'Asociación Valle Verde', price: 5.00, stock: 44, unit: 'unidad', emoji: '🌽', tint: 'sun' },
  { id: 8, name: 'Fresas frescas', category: 'Frutas', farmer: 'Granja La Esperanza', price: 11.90, stock: 22, unit: 'kg', emoji: '🍓', tint: 'rose' }
];
const INITIAL_FARMERS: Farmer[] = [
  { id: 1, name: 'Fundo Los Olivos', location: 'Huaral, Lima', approved: true },
  { id: 2, name: 'Huerto Santa Rosa', location: 'Cañete, Lima', approved: true },
  { id: 3, name: 'Asociación Valle Verde', location: 'Huancayo, Junín', approved: true },
  { id: 4, name: 'Granja La Esperanza', location: 'Lurín, Lima', approved: false }
];
const INITIAL_ORDERS: Order[] = [
  { id: 1001, customer: 'María Torres', address: 'Lima', date: '2026-10-18', lines: [{ name: 'Palta Hass', farmer: 'Fundo Los Olivos', quantity: 2, price: 9.50 }], total: 19, status: 'En preparación' },
  { id: 1002, customer: 'Carlos Ruiz', address: 'Lima', date: '2026-10-19', lines: [{ name: 'Papa amarilla', farmer: 'Asociación Valle Verde', quantity: 3, price: 5.80 }], total: 17.40, status: 'Entregado' }
];

@Injectable({ providedIn: 'root' })
export class DemoStore {
  private readonly prefix = 'agrodirecto-demo-v1-';
  readonly products = signal<Product[]>(this.load('products', INITIAL_PRODUCTS));
  readonly cart = signal<CartItem[]>(this.load('cart', []));
  readonly orders = signal<Order[]>(this.load('orders', INITIAL_ORDERS));
  readonly farmers = signal<Farmer[]>(this.load('farmers', INITIAL_FARMERS));
  readonly complaints = signal<Complaint[]>(this.load('complaints', []));
  readonly cartLines = computed(() => this.cart().flatMap(item => {
    const product = this.products().find(p => p.id === item.productId);
    return product ? [{ product, quantity: item.quantity, subtotal: product.price * item.quantity }] : [];
  }));
  readonly cartCount = computed(() => this.cart().reduce((sum, item) => sum + item.quantity, 0));
  readonly cartTotal = computed(() => this.cartLines().reduce((sum, line) => sum + line.subtotal, 0));

  private load<T>(key: string, fallback: T): T {
    try {
      const raw = localStorage.getItem(this.prefix + key);
      return raw ? JSON.parse(raw) as T : fallback;
    } catch { return fallback; }
  }
  private save<T>(key: string, value: T): void {
    try { localStorage.setItem(this.prefix + key, JSON.stringify(value)); } catch { /* Storage disabled: in-memory demo still works. */ }
  }

  changeQuantity(productId: number, delta: number): void {
    const product = this.products().find(p => p.id === productId);
    if (!product || product.stock < 1) return;
    const current = this.cart().find(item => item.productId === productId)?.quantity ?? 0;
    const next = Math.max(0, Math.min(product.stock, current + delta));
    const updated = this.cart().filter(item => item.productId !== productId);
    if (next > 0) updated.push({ productId, quantity: next });
    this.cart.set(updated);
    this.save('cart', updated);
  }
  removeFromCart(productId: number): void {
    const updated = this.cart().filter(item => item.productId !== productId);
    this.cart.set(updated);
    this.save('cart', updated);
  }
  checkout(customer: string, address: string): string {
    const lines = this.cartLines();
    if (!customer.trim() || !address.trim()) return 'Ingresa tu nombre y dirección.';
    if (!lines.length) return 'Tu carrito está vacío.';
    if (lines.some(line => line.quantity > line.product.stock)) return 'El stock cambió. Revisa las cantidades de tu carrito.';
    const order: Order = {
      id: Math.max(1002, ...this.orders().map(o => o.id)) + 1,
      customer: customer.trim(),
      address: address.trim(),
      date: new Date().toISOString().slice(0, 10),
      lines: lines.map(({ product, quantity }) => ({ name: product.name, farmer: product.farmer, quantity, price: product.price })),
      total: this.cartTotal(),
      status: 'Pendiente'
    };
    const products = this.products().map(product => {
      const line = lines.find(item => item.product.id === product.id);
      return line ? { ...product, stock: product.stock - line.quantity } : product;
    });
    this.products.set(products);
    this.save('products', products);
    this.orders.update(orders => [order, ...orders]);
    this.save('orders', this.orders());
    this.cart.set([]);
    this.save('cart', []);
    return 'Pedido #' + order.id + ' registrado correctamente (demostración local).';
  }
  updateStock(productId: number, delta: number): void {
    const updated = this.products().map(p => p.id === productId ? { ...p, stock: Math.max(0, p.stock + delta) } : p);
    this.products.set(updated);
    this.save('products', updated);
  }
  updateOrderStatus(orderId: number, status: OrderStatus): void {
    const updated = this.orders().map(o => o.id === orderId ? { ...o, status } : o);
    this.orders.set(updated);
    this.save('orders', updated);
  }
  toggleFarmer(farmerId: number): void {
    const updated = this.farmers().map(f => f.id === farmerId ? { ...f, approved: !f.approved } : f);
    this.farmers.set(updated);
    this.save('farmers', updated);
  }
  addComplaint(customer: string, message: string): boolean {
    if (!customer.trim() || !message.trim()) return false;
    const complaint: Complaint = { id: Date.now(), customer: customer.trim(), message: message.trim(), date: new Date().toISOString().slice(0, 10) };
    this.complaints.update(items => [complaint, ...items]);
    this.save('complaints', this.complaints());
    return true;
  }
  reset(): void {
    this.products.set(INITIAL_PRODUCTS);
    this.cart.set([]);
    this.orders.set(INITIAL_ORDERS);
    this.farmers.set(INITIAL_FARMERS);
    this.complaints.set([]);
    this.save('products', INITIAL_PRODUCTS);
    this.save('cart', []);
    this.save('orders', INITIAL_ORDERS);
    this.save('farmers', INITIAL_FARMERS);
    this.save('complaints', []);
  }
}
