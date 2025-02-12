import { useState } from "react";

export default function VisualizarChamado() {
  const [chamado, setChamado] = useState({
    id: 101,
    titulo: "Erro no login",
    descricao: "Usuário não consegue acessar o sistema após atualização",
    categoria: "Bug",
    prioridade: "Alta",
    status: "Em Andamento",
    cliente: "Empresa A",
    atribuidoPara: "João Silva",
    dataAbertura: "05/02/2025",
    dataVencimento: "12/02/2025",
    dataFechamento: "",
    historico: [
      { data: "05/02/2025 14:30", autor: "Sistema", mensagem: "Chamado criado" },
      { data: "05/02/2025 15:45", autor: "João Silva", mensagem: "Iniciada análise do problema" },
      { data: "06/02/2025 10:20", autor: "João Silva", mensagem: "Identificada causa do erro" }
    ]
  });

  const [editMode, setEditMode] = useState(false);
  const [novoComentario, setNovoComentario] = useState("");

  const handleSave = () => {
    setEditMode(false);
    // Aqui você adicionaria a lógica para salvar as alterações
  };

  const handleAddComentario = () => {
    if (novoComentario.trim()) {
      const novoHistorico = {
        data: new Date().toLocaleString(),
        autor: "João Silva",
        mensagem: novoComentario
      };
      setChamado(prev => ({
        ...prev,
        historico: [...prev.historico, novoHistorico]
      }));
      setNovoComentario("");
    }
  };

  return (
    <div className="flex min-h-screen">
      {/* Sidebar */}
      <div className="w-64 bg-gray-900 text-white p-6">
        <h2 className="text-xl font-bold mb-4">Support Flow.AI</h2>
        <nav>
          <ul>
            <li className="mb-2"><a href="#" className="text-gray-300 hover:text-white">Dashboard</a></li>
            <li className="mb-2"><a href="#" className="text-gray-300 hover:text-white">Meus Chamados</a></li>
          </ul>
        </nav>
      </div>

      {/* Content */}
      <div className="flex-1 p-6 bg-gray-100">
        <div className="bg-white shadow-md rounded-lg p-6">
          {/* Header */}
          <div className="flex justify-between items-center mb-6">
            <h1 className="text-2xl font-bold">Chamado #{chamado.id}</h1>
            <div className="space-x-2">
              {editMode ? (
                <>
                  <button 
                    onClick={handleSave}
                    className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600"
                  >
                    Salvar
                  </button>
                  <button 
                    onClick={() => setEditMode(false)}
                    className="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600"
                  >
                    Cancelar
                  </button>
                </>
              ) : (
                <button 
                  onClick={() => setEditMode(true)}
                  className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
                >
                  Editar
                </button>
              )}
            </div>
          </div>

          {/* Main Info */}
          <div className="grid grid-cols-2 gap-6 mb-6">
            <div>
              <div className="mb-4">
                <label className="block text-sm font-medium text-gray-700 mb-1">Título</label>
                {editMode ? (
                  <input
                    type="text"
                    value={chamado.titulo}
                    onChange={(e) => setChamado(prev => ({...prev, titulo: e.target.value}))}
                    className="w-full p-2 border rounded"
                  />
                ) : (
                  <p className="text-gray-900">{chamado.titulo}</p>
                )}
              </div>

              <div className="mb-4">
                <label className="block text-sm font-medium text-gray-700 mb-1">Cliente</label>
                <p className="text-gray-900">{chamado.cliente}</p>
              </div>

              <div className="mb-4">
                <label className="block text-sm font-medium text-gray-700 mb-1">Status</label>
                {editMode ? (
                  <select 
                    value={chamado.status}
                    onChange={(e) => setChamado(prev => ({...prev, status: e.target.value}))}
                    className="w-full p-2 border rounded"
                  >
                    <option>Novo</option>
                    <option>Em Andamento</option>
                    <option>Aguardando Cliente</option>
                    <option>Resolvido</option>
                    <option>Fechado</option>
                  </select>
                ) : (
                  <p className="text-gray-900">{chamado.status}</p>
                )}
              </div>
            </div>

            <div>
              <div className="mb-4">
                <label className="block text-sm font-medium text-gray-700 mb-1">Prioridade</label>
                {editMode ? (
                  <select 
                    value={chamado.prioridade}
                    onChange={(e) => setChamado(prev => ({...prev, prioridade: e.target.value}))}
                    className="w-full p-2 border rounded"
                  >
                    <option>Baixa</option>
                    <option>Média</option>
                    <option>Alta</option>
                  </select>
                ) : (
                  <p className={`text-gray-900 ${
                    chamado.prioridade === "Alta" ? "text-red-600" : 
                    chamado.prioridade === "Média" ? "text-yellow-600" : 
                    "text-green-600"
                  }`}>
                    {chamado.prioridade}
                  </p>
                )}
              </div>

              <div className="mb-4">
                <label className="block text-sm font-medium text-gray-700 mb-1">Categoria</label>
                {editMode ? (
                  <select 
                    value={chamado.categoria}
                    onChange={(e) => setChamado(prev => ({...prev, categoria: e.target.value}))}
                    className="w-full p-2 border rounded"
                  >
                    <option>Bug</option>
                    <option>Suporte Técnico</option>
                    <option>Financeiro</option>
                    <option>Dúvida</option>
                  </select>
                ) : (
                  <p className="text-gray-900">{chamado.categoria}</p>
                )}
              </div>

              <div className="mb-4">
                <label className="block text-sm font-medium text-gray-700 mb-1">Atribuído para</label>
                {editMode ? (
                  <input
                    type="text"
                    value={chamado.atribuidoPara}
                    onChange={(e) => setChamado(prev => ({...prev, atribuidoPara: e.target.value}))}
                    className="w-full p-2 border rounded"
                  />
                ) : (
                  <p className="text-gray-900">{chamado.atribuidoPara}</p>
                )}
              </div>
            </div>
          </div>

          {/* Descrição */}
          <div className="mb-6">
            <label className="block text-sm font-medium text-gray-700 mb-1">Descrição</label>
            {editMode ? (
              <textarea
                value={chamado.descricao}
                onChange={(e) => setChamado(prev => ({...prev, descricao: e.target.value}))}
                className="w-full p-2 border rounded h-32"
              />
            ) : (
              <p className="text-gray-900 whitespace-pre-wrap">{chamado.descricao}</p>
            )}
          </div>

          {/* Histórico e Comentários */}
          <div className="mt-8">
            <h2 className="text-xl font-bold mb-4">Histórico</h2>
            <div className="space-y-4 mb-4">
              {chamado.historico.map((item, index) => (
                <div key={index} className="bg-gray-50 p-4 rounded">
                  <div className="flex justify-between text-sm text-gray-500 mb-1">
                    <span>{item.autor}</span>
                    <span>{item.data}</span>
                  </div>
                  <p className="text-gray-900">{item.mensagem}</p>
                </div>
              ))}
            </div>

            {/* Novo Comentário */}
            <div className="mt-4">
              <textarea
                value={novoComentario}
                onChange={(e) => setNovoComentario(e.target.value)}
                placeholder="Adicione um comentário..."
                className="w-full p-2 border rounded h-24 mb-2"
              />
              <button
                onClick={handleAddComentario}
                className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
              >
                Adicionar Comentário
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}