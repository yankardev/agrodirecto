package pe.cibertec.agrodirecto.order.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.cibertec.agrodirecto.order.dto.PedidoResponse;
import pe.cibertec.agrodirecto.order.entity.Pedido;
import pe.cibertec.agrodirecto.order.repository.PedidoRepository;
import pe.cibertec.agrodirecto.order.service.PedidoService;
import pe.cibertec.agrodirecto.order.messaging.PedidoEventPublisher;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final PedidoEventPublisher pedidoEventPublisher;

    @Override
    public List<PedidoResponse> listarPorCliente(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId)
            .stream()
            .map(this::convertir)
            .toList();
    }

    @Override
    public List<PedidoResponse> listarPorAgricultor(Long agricultorId) {
        return pedidoRepository.findByAgricultorId(agricultorId)
            .stream()
            .map(this::convertir)
            .toList();
    }

    @Override
    @Transactional
    public PedidoResponse actualizarEstado(Long pedidoId, String estado) {

        Pedido pedido = pedidoRepository.findById(pedidoId)
            .orElseThrow(() ->
                new RuntimeException("Pedido no encontrado"));

        pedido.setEstado(estado.toUpperCase());

        pedido = pedidoRepository.save(pedido);

        pedidoEventPublisher.publicar(
            "PEDIDO_ACTUALIZADO:" +
                pedido.getId() +
                ":" +
                pedido.getEstado()
        );

        return convertir(pedido);
    }

    private PedidoResponse convertir(Pedido pedido) {
        return new PedidoResponse(
            pedido.getId(),
            pedido.getClienteId(),
            pedido.getAgricultorId(),
            pedido.getEstado(),
            pedido.getTotal(),
            pedido.getFecha()
        );
    }
}
