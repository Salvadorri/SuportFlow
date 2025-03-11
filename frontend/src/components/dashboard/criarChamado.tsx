// src/components/dashboard/criarChamado.tsx
import { Link } from "@tanstack/react-router";
import logo from "../../assets/logo.png";
import LogoutButton from "./logoutbutton";

  const menuItems = [
    { label: "Abrir Chamado", href: "/criar-chamado", destacado: true },
    { label: "Histórico Chamados", href: "/chamados-historico" },
    { label: "Chat", href: "/chat-chamado" }
];



  const CriarChamado = () => {
    return (
      <div className="flex h-screen">
        {/* Sidebar */}
        <aside className="w-64 bg-gray-900 text-white p-5">
        <div className="flex items-center mb-6">
        <Link to="/dashboard">{/* Added Link here */}
          <img src={logo} alt="SupportFlow Logo" className="h-14 w-auto mr-2 cursor-pointer" />
          </Link>
          <h1 className="text-2xl font-bold">SupportFlow</h1>
        </div>
        <nav>
          <ul>
            {menuItems.map((item) => (
              <li
              key={item.label}
              className={`mb-2 p-2 rounded ${
                item.destacado
                  ? "bg-green-700"
                  : "hover:bg-green-700"
              }`}
            >
                <Link to={item.href} className="text-white hover:text-white block w-full h-full">
                  {item.label}
                </Link>
              </li>
            ))}
          </ul>
        </nav>
        <div className="mt-auto"> {/* Use mt-auto para empurrar para o final */}
       <LogoutButton userType="user" />
    </div>
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
                rows={4} // Changed from string to number
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