import { Link } from "@tanstack/react-router";

const CriarChamado = () => {
  return (
    <div className="flex h-screen">
      {/* Sidebar */}
      <aside className="w-64 bg-gray-900 text-white p-5">
        <h1 className="text-2xl font-bold mb-6">Support Flow.AI</h1>
        <nav>
          <ul>
            <li className="mb-4 font-semibold">Chamados:</li>

            <li className="mb-2 hover:bg-gray-700 p-2 rounded">
  <Link to="/dashboard" className="text-blue-400 block w-full h-full">
    Dashboard
  </Link>
</li>
            
            <li className="mb-2 hover:bg-gray-700 p-2 rounded">
              <a href="#" className="text-blue-400">Meus Chamados</a>
            </li>
          </ul>
        </nav>
      </aside>
      
      {/* Main Content */}
      <main className="flex-1 p-6 bg-white">
        <h2 className="text-3xl font-semibold mb-6">Criar Chamado</h2>
        
        <div className="bg-white p-6 rounded-lg shadow-md max-w-4xl">
          <form className="space-y-4">
            <div>
              <label className="block text-gray-700 mb-2">Título do Chamado *</label>
              <input 
                type="text" 
                className="w-full p-2 border rounded focus:ring-2 focus:ring-blue-500" 
                placeholder="Digite o título" 
                required 
              />
            </div>

            <div>
              <label className="block text-gray-700 mb-2">Descrição *</label>
              <textarea 
                className="w-full p-2 border rounded focus:ring-2 focus:ring-blue-500" 
                rows="4" 
                placeholder="Descreva o problema" 
                required
              ></textarea>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label className="block text-gray-700 mb-2">Prioridade *</label>
                <select className="w-full p-2 border rounded focus:ring-2 focus:ring-blue-500">
                  <option>Baixa</option>
                  <option>Média</option>
                  <option>Alta</option>
                </select>
              </div>
              <div>
                <label className="block text-gray-700 mb-2">Categoria *</label>
                <select className="w-full p-2 border rounded focus:ring-2 focus:ring-blue-500">
                  <option>Suporte Técnico</option>
                  <option>Financeiro</option>
                  <option>Outro</option>
                </select>
              </div>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label className="block text-gray-700 mb-2">Atribuir para *</label>
                <input 
                  type="text" 
                  className="w-full p-2 border rounded focus:ring-2 focus:ring-blue-500" 
                  placeholder="Nome do atendente" 
                  required 
                />
              </div>
              <div>
                <label className="block text-gray-700 mb-2">Data de Vencimento *</label>
                <input 
                  type="date" 
                  className="w-full p-2 border rounded focus:ring-2 focus:ring-blue-500" 
                  required 
                />
              </div>
            </div>
            
            <div className="mt-6 flex gap-4">
              <button 
                type="submit" 
                className="px-6 py-2 bg-green-500! text-white rounded hover:bg-green-600 transition-colors duration-200"
              >
                Salvar
              </button>
              <button 
                type="button" 
                className="px-6 py-2 bg-red-500! text-white rounded hover:bg-red-600 transition-colors duration-200"
              >
                Cancelar
              </button>
            </div>

            <p className="text-sm text-gray-500 mt-4">* Campos obrigatórios</p>
          </form>
        </div>
      </main>
    </div>
  );
};


export default CriarChamado;
