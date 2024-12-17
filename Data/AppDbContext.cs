using Microsoft.EntityFrameworkCore;

namespace PedidosAgendamentosMicroservice
{
    public class AppDbContext : DbContext
    {
        public AppDbContext(DbContextOptions<AppDbContext> options) : base(options) { }

        public DbSet<Pedido> Pedidos { get; set; }
    }
}