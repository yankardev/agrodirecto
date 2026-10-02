package pe.cibertec.agrodirecto.order.service;

import pe.cibertec.agrodirecto.order.dto.PedidoResponse;

import java.util.List;

public interface PedidoService {

    List<PedidoResponse> listarPorCliente(Long clienteId);

    List<PedidoResponse> listarPorAgricultor(Long agricultorId);

    PedidoResponse actualizarEstado(Long pedidoId, String estado);
}
