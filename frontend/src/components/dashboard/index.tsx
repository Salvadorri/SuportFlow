import ProdutosEServicos from "./produtosServicos";
import homeIcon from "../../assets/home.png"; // Importe a imagem do ícone

export default function Index() {
  return (
    <div className="flex flex-col h-screen">
      {/* Header */}
      <header className="bg-gray-800 text-white py-4 px-6 text-center text-xl font-semibold">
        SupportFlowAI
      </header>

      <div className="flex flex-1">
       {/* Sidebar */}
       <aside className="w-64 bg-gray-900 text-white p-6 flex-shrink-0">
          {/* Substitua o texto "Home" por uma tag <img> */}
          <div className="flex items-center mb-6 pl-0 pr-0">
            <img src={homeIcon} alt="Home" className="h-20 w-20" /> 
            {/* Ajuste w-6 e h-6 para o tamanho desejado do ícone */}
            <h1 className="text-xl font-bold pr-2">Home</h1>
          </div>
          <nav className="space-y-4">
            <div>
              <ul className="ml-2 space-y-2">
                <li>Empresas</li>
              </ul>
            </div>
            <div>
              <ul className="ml-2 space-y-2">
                <li>Atendentes</li>
              </ul>
            </div>
            <div>
              <ul className="ml-2 space-y-2">
                <li>Clientes</li>
              </ul>
            </div>
            <div>
              <ProdutosEServicos />
            </div>
            <div>
              <ul className="ml-2 space-y-2">
                <li>Chamados</li>
              </ul>
            </div>
            <div>
              <ul className="ml-2 space-y-2">
                <li>Relatorios</li>
              </ul>
            </div>
            <div>
              <ul className="ml-2 space-y-2">
                <li>Chat AI</li>
              </ul>
            </div>
          </nav>
        </aside>

        {/* Main Content */}
        <main className="flex-1 bg-gray-100 p-10 overflow-auto">
          <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
            <div className="bg-white shadow-lg p-6 rounded-xl text-center">
              <h3 className="text-lg font-bold">Produtos e Serviços</h3>
              <p className="text-gray-600">Gerencie seus produtos e serviços.</p>
            </div>
            <div className="bg-white shadow-lg p-6 rounded-xl text-center">
              <h3 className="text-lg font-bold">Funcionários</h3>
              <p className="text-gray-600">
                Crie, edite ou exclua logins dos funcionários.
              </p>
            </div>
            <div className="bg-white shadow-lg p-6 rounded-xl text-center">
              <h3 className="text-lg font-bold">Relatórios</h3>
              <p className="text-gray-600">
                Acompanhe o desempenho e histórico de interações.
              </p>
            </div>
          </div>

          <div className="mt-8 flex flex-wrap justify-center gap-4">
            <button className="bg-blue-500 text-white px-6 py-3 rounded-lg shadow-md hover:bg-blue-600 transition">
              + Criar Produto ou Serviço
            </button>
            <button className="bg-blue-500 text-white px-6 py-3 rounded-lg shadow-md hover:bg-blue-600 transition">
              + Adicionar Funcionário
            </button>
            <button className="bg-blue-500 text-white px-6 py-3 rounded-lg shadow-md hover:bg-blue-600 transition">
              + Configurar Prioridades
            </button>
          </div>
        </main>
      </div>

      {/* Footer */}
      <footer className="bg-gray-800 text-white text-center py-3 text-sm">
        © 2025 SupportFlow - Todos os direitos reservados.
      </footer>
    </div>
  );
}