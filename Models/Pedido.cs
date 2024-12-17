using System;
using System.ComponentModel.DataAnnotations;

namespace PedidosAgendamentosMicroservice
{
    public class Pedido
    {
        [Key]
        public int Id { get; set; }

        [Required]
        public string UsuarioId { get; set; } // Referência ao usuário que fez o pedido

        [Required]
        public string ProdutoServico { get; set; }

        [Required]
        public DateTime DataPedido { get; set; }

        public DateTime? DataEntrega { get; set; } // Opcional

        public string Status { get; set; } = "Pendente";
    }
}