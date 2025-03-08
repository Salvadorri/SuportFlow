import React, { useState, useEffect, useRef } from 'react';
import { Link } from "@tanstack/react-router";

interface Chamado {
  id: number;
  titulo: string;
  categoria: string;
  status: 'Novo' | 'Em Andamento' | 'Aguardando Cliente' | 'Resolvido' | 'Fechado';
  prioridade: 'Alta' | 'Média' | 'Baixa';
  dataAbertura: string;
  descricao: string;
}

interface Mensagem {
  id: number;
  sender: 'cliente' | 'atendente';
  text: string;
  timestamp: Date;
}

interface Chat {
  id: number;
  chamadoId: number;
  mensagens: Mensagem[];
  ativo: boolean;
}

const DashboardCliente: React.FC = () => {
  const [chamados, setChamados] = useState<Chamado[]>([]);
  const [meusChamados, setMeusChamados] = useState<Chamado[]>([]);
  const [chatAberto, setChatAberto] = useState<Chat | null>(null);
  const [novoTitulo, setNovoTitulo] = useState('');
  const [novaCategoria, setNovaCategoria] = useState('Bug');
  const [novaDescricao, setNovaDescricao] = useState('');
  const [mensagem, setMensagem] = useState('');
  const [mostrarFormulario, setMostrarFormulario] = useState(false);
  const [chamadoSelecionado, setChamadoSelecionado] = useState<Chamado | null>(null);
  const [aba, setAba] = useState<'chamados' | 'chat'>('chamados');
  const mensagensEndRef = useRef<null | HTMLDivElement>(null);

  // Simular carregamento de dados
  useEffect(() => {
    const mockChamados: Chamado[] = [
      {
        id: 1001,
        titulo: "Falha ao gerar relatório",
        categoria: "Bug",
        status: "Em Andamento",
        prioridade: "Alta",
        dataAbertura: "01/03/2025",
        descricao: "O sistema apresenta erro ao tentar gerar relatório mensal de vendas."
      },
      {
        id: 1002,
        titulo: "Dúvida sobre faturamento",
        categoria: "Financeiro", 
        status: "Aguardando Cliente",
        prioridade: "Média",
        dataAbertura: "28/02/2025",
        descricao: "Preciso entender como emitir uma nota fiscal para cliente estrangeiro."
      },
      {
        id: 1003,
        titulo: "Acesso a módulo de estoque",
        categoria: "Suporte Técnico",
        status: "Novo",
        prioridade: "Baixa",
        dataAbertura: "05/03/2025",
        descricao: "Nosso novo funcionário precisa de acesso ao módulo de estoque."
      }
    ];

    const mockChat: Chat = {
      id: 1,
      chamadoId: 1001,
      ativo: true,
      mensagens: [
        {
          id: 1,
          sender: 'cliente',
          text: 'Olá, estou tendo problemas com o relatório mensal. Podem me ajudar?',
          timestamp: new Date(2025, 2, 1, 9, 30)
        },
        {
          id: 2,
          sender: 'atendente',
          text: 'Bom dia! Claro, estamos verificando o problema. Pode me dizer qual relatório específico está tentando gerar?',
          timestamp: new Date(2025, 2, 1, 9, 35)
        },
        {
          id: 3,
          sender: 'cliente',
          text: 'É o relatório de vendas por região. Quando tento gerar para fevereiro, aparece uma mensagem de erro.',
          timestamp: new Date(2025, 2, 1, 9, 37)
        },
        {
          id: 4,
          sender: 'atendente',
          text: 'Entendi. Nossa equipe técnica já identificou o problema e está trabalhando em uma correção. Deve ser resolvido até o final do dia de hoje.',
          timestamp: new Date(2025, 2, 1, 9, 40)
        }
      ]
    };

    setChamados(mockChamados);
    setMeusChamados(mockChamados);
    setChatAberto(mockChat);
  }, []);

  useEffect(() => {
    scrollToBottom();
  }, [chatAberto]);

  const scrollToBottom = () => {
    mensagensEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  };

  const formatarData = (data: Date): string => {
    return data.toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' });
  };

  const abrirChamado = () => {
    if (novoTitulo.trim() === '' || novaDescricao.trim() === '') {
      alert('Por favor, preencha todos os campos obrigatórios.');
      return;
    }

    const novoChamado: Chamado = {
      id: Math.floor(1000 + Math.random() * 9000),
      titulo: novoTitulo,
      categoria: novaCategoria,
      status: 'Novo',
      prioridade: 'Média',
      dataAbertura: new Date().toLocaleDateString('pt-BR'),
      descricao: novaDescricao
    };

    const novosChamados = [...chamados, novoChamado];
    setChamados(novosChamados);
    setMeusChamados(novosChamados);
    setNovoTitulo('');
    setNovaCategoria('Bug');
    setNovaDescricao('');
    setMostrarFormulario(false);
  };

  const enviarMensagem = () => {
    if (!chatAberto || mensagem.trim() === '') return;

    const novaMensagem: Mensagem = {
      id: chatAberto.mensagens.length + 1,
      sender: 'cliente',
      text: mensagem,
      timestamp: new Date()
    };

    const chatAtualizado = {
      ...chatAberto,
      mensagens: [...chatAberto.mensagens, novaMensagem]
    };

    setChatAberto(chatAtualizado);
    setMensagem('');
    
    // Simular resposta do atendente após um tempo
    setTimeout(() => {
      if (chatAberto) {
        const respostaAtendente: Mensagem = {
          id: chatAtualizado.mensagens.length + 1,
          sender: 'atendente',
          text: 'Estamos analisando sua solicitação. Em breve retornaremos com mais informações.',
          timestamp: new Date()
        };

        const chatComResposta = {
          ...chatAtualizado,
          mensagens: [...chatAtualizado.mensagens, respostaAtendente]
        };

        setChatAberto(chatComResposta);
      }
    }, 3000);
  };

  const verDetalhes = (chamado: Chamado) => {
    setChamadoSelecionado(chamado);
  };

  const iniciarChat = (chamado: Chamado) => {
    // Verificar se já existe um chat para este chamado
    if (chatAberto && chatAberto.chamadoId === chamado.id) {
      setAba('chat');
      return;
    }

    // Criar novo chat
    const novoChat: Chat = {
      id: Math.floor(1 + Math.random() * 1000),
      chamadoId: chamado.id,
      ativo: true,
      mensagens: []
    };

    setChatAberto(novoChat);
    setAba('chat');
  };

  const getStatusClass = (status: string) => {
    switch (status) {
      case 'Novo':
        return 'bg-blue-100 text-blue-800';
      case 'Em Andamento':
        return 'bg-yellow-100 text-yellow-800';
      case 'Aguardando Cliente':
        return 'bg-purple-100 text-purple-800';
      case 'Resolvido':
        return 'bg-green-100 text-green-800';
      case 'Fechado':
        return 'bg-gray-100 text-gray-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  };

  const getPrioridadeClass = (prioridade: string) => {
    switch (prioridade) {
      case 'Alta':
        return 'text-red-600';
      case 'Média':
        return 'text-yellow-600';
      case 'Baixa':
        return 'text-green-600';
      default:
        return 'text-gray-600';
    }
  };

  return (
    <div className="flex min-h-screen bg-gray-100">
      {/* Sidebar */}
      <div className="w-64 bg-gray-900 text-white p-6">
        <h2 className="text-xl font-bold mb-6">Support Flow</h2>
        <div className="mb-4">
          <p className="text-sm text-gray-400">Cliente:</p>
          <p className="font-medium">Empresa ABC Ltda.</p>
        </div>
        <nav>
          <ul className="space-y-2">
            <li className={`p-2 rounded cursor-pointer ${aba === 'chamados' ? 'bg-blue-600' : 'hover:bg-gray-700'}`} onClick={() => setAba('chamados')}>
              Meus Chamados
            </li>
            <li className={`p-2 rounded cursor-pointer ${aba === 'chat' ? 'bg-blue-600' : 'hover:bg-gray-700'}`} onClick={() => setAba('chat')}>
              Chat com Suporte
            </li>
          </ul>
        </nav>
        <div className="mt-auto pt-6">
          <button 
            onClick={() => setMostrarFormulario(true)}
            className="w-full bg-blue-500 hover:bg-blue-600 text-white py-2 px-4 rounded-md"
          >
            + Novo Chamado
          </button>
        </div>
      </div>

      {/* Main Content */}
      <div className="flex-1">
        {/* Header */}
        <header className="bg-white p-4 shadow-md flex justify-between items-center">
          <h1 className="text-xl font-bold">Área do Cliente</h1>
          <div className="flex items-center space-x-4">
            <span className="text-green-500">●</span>
            <span>Online</span>
          </div>
        </header>

        {/* Main content area */}
        <main className="p-6">
          {aba === 'chamados' ? (
            <>
              {/* Chamados View */}
              <div className="bg-white shadow-md rounded-lg p-6">
                <div className="flex justify-between items-center mb-6">
                  <h2 className="text-xl font-bold">Meus Chamados</h2>
                </div>

                {/* Lista de chamados */}
                <div className="overflow-x-auto">
                  <table className="w-full border-collapse">
                    <thead>
                      <tr className="bg-gray-50 text-left">
                        <th className="p-3 border-b">ID</th>
                        <th className="p-3 border-b">Título</th>
                        <th className="p-3 border-b">Categoria</th>
                        <th className="p-3 border-b">Status</th>
                        <th className="p-3 border-b">Prioridade</th>
                        <th className="p-3 border-b">Data</th>
                        <th className="p-3 border-b">Ações</th>
                      </tr>
                    </thead>
                    <tbody>
                      {meusChamados.map((chamado) => (
                        <tr key={chamado.id} className="border-b hover:bg-gray-50">
                          <td className="p-3">#{chamado.id}</td>
                          <td className="p-3">{chamado.titulo}</td>
                          <td className="p-3">{chamado.categoria}</td>
                          <td className="p-3">
                            <span className={`px-2 py-1 rounded-full text-xs ${getStatusClass(chamado.status)}`}>
                              {chamado.status}
                            </span>
                          </td>
                          <td className={`p-3 ${getPrioridadeClass(chamado.prioridade)}`}>
                            {chamado.prioridade}
                          </td>
                          <td className="p-3">{chamado.dataAbertura}</td>
                          <td className="p-3 flex space-x-2">
                            <button 
                              onClick={() => verDetalhes(chamado)}
                              className="bg-blue-100 text-blue-600 px-2 py-1 rounded text-sm hover:bg-blue-200"
                            >
                              Detalhes
                            </button>
                            <button 
                              onClick={() => iniciarChat(chamado)}
                              className="bg-green-100 text-green-600 px-2 py-1 rounded text-sm hover:bg-green-200"
                            >
                              Chat
                            </button>
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>

                {/* Detalhes do chamado (quando selecionado) */}
                {chamadoSelecionado && (
                  <div className="mt-6 border rounded-lg p-4 bg-gray-50">
                    <div className="flex justify-between items-center mb-4">
                      <h3 className="text-lg font-semibold">Detalhes do Chamado #{chamadoSelecionado.id}</h3>
                      <button 
                        onClick={() => setChamadoSelecionado(null)}
                        className="text-gray-500 hover:text-gray-700"
                      >
                        ✕
                      </button>
                    </div>
                    <div className="grid grid-cols-2 gap-4 mb-4">
                      <div>
                        <p className="text-sm text-gray-500">Título:</p>
                        <p>{chamadoSelecionado.titulo}</p>
                      </div>
                      <div>
                        <p className="text-sm text-gray-500">Status:</p>
                        <p>
                          <span className={`px-2 py-1 rounded-full text-xs ${getStatusClass(chamadoSelecionado.status)}`}>
                            {chamadoSelecionado.status}
                          </span>
                        </p>
                      </div>
                      <div>
                        <p className="text-sm text-gray-500">Categoria:</p>
                        <p>{chamadoSelecionado.categoria}</p>
                      </div>
                      <div>
                        <p className="text-sm text-gray-500">Prioridade:</p>
                        <p className={getPrioridadeClass(chamadoSelecionado.prioridade)}>
                          {chamadoSelecionado.prioridade}
                        </p>
                      </div>
                    </div>
                    <div>
                      <p className="text-sm text-gray-500">Descrição:</p>
                      <p className="mt-1 p-2 bg-white border rounded">
                        {chamadoSelecionado.descricao}
                      </p>
                    </div>
                    <div className="mt-4 flex justify-end">
                      <button 
                        onClick={() => iniciarChat(chamadoSelecionado)}
                        className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600"
                      >
                        Iniciar Chat com Suporte
                      </button>
                    </div>
                  </div>
                )}
              </div>
            </>
          ) : (
            <>
              {/* Chat View */}
              <div className="bg-white shadow-md rounded-lg h-[calc(100vh-180px)] flex flex-col">
                {/* Chat Header */}
                <div className="bg-gray-50 p-4 border-b flex justify-between items-center">
                  <div>
                    <h2 className="font-bold">Suporte Técnico</h2>
                    {chatAberto && chatAberto.chamadoId && (
                      <p className="text-sm text-gray-500">
                        Chamado #{chatAberto.chamadoId} - {chamados.find(c => c.id === chatAberto?.chamadoId)?.titulo}
                      </p>
                    )}
                  </div>
                  <div>
                    <span className="inline-block w-2 h-2 rounded-full bg-green-500 mr-2"></span>
                    <span className="text-sm text-gray-600">Atendente online</span>
                  </div>
                </div>
                
                {/* Messages Area */}
                <div className="flex-1 p-4 overflow-y-auto bg-gray-50">
                  {chatAberto && chatAberto.mensagens.length > 0 ? (
                    <>
                      {chatAberto.mensagens.map((msg) => (
                        <div 
                          key={msg.id} 
                          className={`mb-4 flex ${msg.sender === 'cliente' ? 'justify-end' : 'justify-start'}`}
                        >
                          <div 
                            className={`max-w-md rounded-lg p-3 ${
                              msg.sender === 'cliente' 
                                ? 'bg-blue-500 text-white' 
                                : 'bg-white border border-gray-200'
                            }`}
                          >
                            <p>{msg.text}</p>
                            <p className={`text-xs mt-1 text-right ${
                              msg.sender === 'cliente' ? 'text-blue-100' : 'text-gray-500'
                            }`}>
                              {formatarData(msg.timestamp)}
                            </p>
                          </div>
                        </div>
                      ))}
                      <div ref={mensagensEndRef} />
                    </>
                  ) : (
                    <div className="h-full flex items-center justify-center">
                      <p className="text-gray-500 text-center">
                        {chatAberto 
                          ? "Nenhuma mensagem ainda. Envie uma mensagem para iniciar a conversa." 
                          : "Selecione um chamado para iniciar uma conversa com o suporte."}
                      </p>
                    </div>
                  )}
                </div>
                
                {/* Input Area */}
                <div className="p-4 border-t">
                  <div className="flex">
                    <input
                      type="text"
                      value={mensagem}
                      onChange={(e) => setMensagem(e.target.value)}
                      onKeyPress={(e) => e.key === 'Enter' && enviarMensagem()}
                      placeholder="Digite sua mensagem..."
                      className="flex-1 border border-gray-300 rounded-l-md px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
                      disabled={!chatAberto}
                    />
                    <button
                      onClick={enviarMensagem}
                      className="bg-blue-500 text-white px-4 py-2 rounded-r-md hover:bg-blue-600 disabled:bg-gray-300"
                      disabled={!chatAberto}
                    >
                      Enviar
                    </button>
                  </div>
                </div>
              </div>
            </>
          )}
        </main>
      </div>

      {/* Modal de Novo Chamado */}
      {mostrarFormulario && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4">
          <div className="bg-white rounded-lg p-6 w-full max-w-md">
            <div className="flex justify-between items-center mb-4">
              <h2 className="text-lg font-bold">Abrir Novo Chamado</h2>
              <button 
                onClick={() => setMostrarFormulario(false)}
                className="text-gray-500 hover:text-gray-700"
              >
                ✕
              </button>
            </div>
            <div className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Título *</label>
                <input
                  type="text"
                  value={novoTitulo}
                  onChange={(e) => setNovoTitulo(e.target.value)}
                  className="w-full p-2 border rounded"
                  placeholder="Descreva o problema brevemente"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Categoria *</label>
                <select
                  value={novaCategoria}
                  onChange={(e) => setNovaCategoria(e.target.value)}
                  className="w-full p-2 border rounded"
                >
                  <option value="Bug">Bug</option>
                  <option value="Suporte Técnico">Suporte Técnico</option>
                  <option value="Financeiro">Financeiro</option>
                  <option value="Dúvida">Dúvida</option>
                </select>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Descrição *</label>
                <textarea
                  value={novaDescricao}
                  onChange={(e) => setNovaDescricao(e.target.value)}
                  className="w-full p-2 border rounded h-32"
                  placeholder="Descreva o problema com mais detalhes"
                />
              </div>
              <div className="flex justify-end space-x-2 pt-4">
                <button
                  onClick={() => setMostrarFormulario(false)}
                  className="px-4 py-2 border border-gray-300 rounded hover:bg-gray-50"
                >
                  Cancelar
                </button>
                <button
                  onClick={abrirChamado}
                  className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600"
                >
                  Abrir Chamado
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default DashboardCliente;