package pe.cibertec.agrodirecto.order.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.cibertec.agrodirecto.order.dto.CheckoutRequest;
import pe.cibertec.agrodirecto.order.dto.PedidoResponse;
import pe.cibertec.agrodirecto.order.entity.Carrito;
import pe.cibertec.agrodirecto.order.entity.DetalleCarrito;
import pe.cibertec.agrodirecto.order.entity.DetallePedido;
import pe.cibertec.agrodirecto.order.entity.Pedido;
import pe.cibertec.agrodirecto.order.repository.CarritoRepository;
import pe.cibertec.agrodirecto.order.repository.DetalleCarritoRepository;
import pe.cibertec.agrodirecto.order.repository.DetallePedidoRepository;
import pe.cibertec.agrodirecto.order.repository.PedidoRepository;
import pe.cibertec.agrodirecto.order.service.CheckoutService;
import pe.cibertec.agrodirecto.order.messaging.PedidoEventPublisher;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CheckoutServiceImpl implements CheckoutService {

    private final CarritoRepository carritoRepository;
    private final DetalleCarritoRepository detalleCarritoRepository;
    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final PedidoEventPublisher pedidoEventPublisher;

    @Override
    @Transactional
    public List<PedidoResponse> procesarCheckout(CheckoutRequest request) {

        Carrito carrito = carritoRepository
            .findByClienteIdAndActivoTrue(request.getClienteId())
            .orElseThrow(() ->
                new RuntimeException("Carrito no encontrado"));

        List<DetalleCarrito> items =
            detalleCarritoRepository.findByCarritoId(carrito.getId());

        if (items.isEmpty()) {
            throw new RuntimeException("El carrito está vacío");
        }

        Map<Long, List<DetalleCarrito>> itemsPorAgricultor =
            items.stream()
                .collect(Collectors.groupingBy(
                    DetalleCarrito::getAgricultorId
                ));

        List<PedidoResponse> respuestas = new ArrayList<>();

        for (Map.Entry<Long, List<DetalleCarrito>> entry
            : itemsPorAgricultor.entrySet()) {

            Long agricultorId = entry.getKey();
            List<DetalleCarrito> itemsAgricultor = entry.getValue();

            BigDecimal total = itemsAgricultor.stream()
                .map(item ->
                    item.getPrecioUnitario()
                        .multiply(
                            BigDecimal.valueOf(
                                item.getCantidad()
                            )
                        )
                )
                .reduce(
                    BigDecimal.ZERO,
                    BigDecimal::add
                );

            Pedido pedido = Pedido.builder()
                .clienteId(request.getClienteId())
                .agricultorId(agricultorId)
                .estado("PENDIENTE")
                .total(total)
                .fecha(LocalDateTime.now())
                .build();

            pedido = pedidoRepository.save(pedido);

            pedidoEventPublisher.publicar(
                "PEDIDO_CREADO:" + pedido.getId()
            );

            for (DetalleCarrito item : itemsAgricultor) {

                BigDecimal subtotal =
                    item.getPrecioUnitario()
                        .multiply(
                            BigDecimal.valueOf(
                                item.getCantidad()
                            )
                        );

                DetallePedido detallePedido =
                    DetallePedido.builder()
                        .pedido(pedido)
                        .productoId(item.getProductoId())
                        .cantidad(item.getCantidad())
                        .precioUnitario(item.getPrecioUnitario())
                        .subtotal(subtotal)
                        .build();

                detallePedidoRepository.save(detallePedido);
            }

            respuestas.add(
                new PedidoResponse(
                    pedido.getId(),
                    pedido.getClienteId(),
                    pedido.getAgricultorId(),
                    pedido.getEstado(),
                    pedido.getTotal(),
                    pedido.getFecha()
                )
            );
        }

        detalleCarritoRepository.deleteByCarritoId(carrito.getId());

        carrito.setActivo(false);
        carritoRepository.save(carrito);

        return respuestas;
    }
}
