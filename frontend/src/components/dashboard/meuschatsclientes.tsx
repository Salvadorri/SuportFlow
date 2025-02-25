import React, { useState, useEffect } from 'react';

interface Chat {
  id: number;
  clientName: string;
  ticketId: number;
  lastMessage: string;
  lastMessageTime: Date;
  status: 'active' | 'waiting' | 'closed';
  unreadCount: number;
}

interface ChatFilter {
  searchTerm: string;
  status: 'all' | 'active' | 'waiting' | 'closed';
}

const ChatDashboard: React.FC = () => {
  const [chats, setChats] = useState<Chat[]>([]);
  const [filteredChats, setFilteredChats] = useState<Chat[]>([]);
  const [selectedChat, setSelectedChat] = useState<Chat | null>(null);
  const [filter, setFilter] = useState<ChatFilter>({
    searchTerm: '',
    status: 'all'
  });

  useEffect(() => {
    // Simulando carregamento de dados - em produção seria uma chamada API
    const mockChats: Chat[] = [
      {
        id: 1,
        clientName: 'Maria Silva',
        ticketId: 5432,
        lastMessage: 'Obrigado pela ajuda!',
        lastMessageTime: new Date(2025, 1, 18, 14, 35),
        status: 'active',
        unreadCount: 2
      },
      {
        id: 2,
        clientName: 'João Oliveira',
        ticketId: 5433,
        lastMessage: 'Ainda estou aguardando a solução...',
        lastMessageTime: new Date(2025, 1, 19, 9, 12),
        status: 'waiting',
        unreadCount: 0
      },
      {
        id: 3,
        clientName: 'Ana Pereira',
        ticketId: 5430,
        lastMessage: 'Problema resolvido, muito obrigada.',
        lastMessageTime: new Date(2025, 1, 17, 16, 45),
        status: 'closed',
        unreadCount: 0
      },
      {
        id: 4,
        clientName: 'Carlos Santos',
        ticketId: 5435,
        lastMessage: 'Preciso de suporte urgente para configurar o sistema.',
        lastMessageTime: new Date(2025, 1, 19, 10, 23),
        status: 'active',
        unreadCount: 3
      },
      {
        id: 5,
        clientName: 'Luiza Mendes',
        ticketId: 5429,
        lastMessage: 'Vou verificar se a solução funcionou e retorno.',
        lastMessageTime: new Date(2025, 1, 18, 11, 17),
        status: 'waiting',
        unreadCount: 0
      }
    ];
    
    setChats(mockChats);
    setFilteredChats(mockChats);
  }, []);

  useEffect(() => {
    applyFilters();
  }, [filter, chats]);

  const applyFilters = () => {
    let result = [...chats];
    
    // Filtrar por status
    if (filter.status !== 'all') {
      result = result.filter(chat => chat.status === filter.status);
    }
    
    // Filtrar por termo de busca (nome do cliente ou ID do chamado)
    if (filter.searchTerm) {
      const searchLower = filter.searchTerm.toLowerCase();
      result = result.filter(chat => 
        chat.clientName.toLowerCase().includes(searchLower) || 
        chat.ticketId.toString().includes(filter.searchTerm)
      );
    }
    
    // Ordenar por não lidos primeiro, depois por data da última mensagem
    result.sort((a, b) => {
      if (a.unreadCount > 0 && b.unreadCount === 0) return -1;
      if (a.unreadCount === 0 && b.unreadCount > 0) return 1;
      return b.lastMessageTime.getTime() - a.lastMessageTime.getTime();
    });
    
    setFilteredChats(result);
  };

  const handleSearch = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFilter({
      ...filter,
      searchTerm: e.target.value
    });
  };

  const handleStatusFilter = (status: 'all' | 'active' | 'waiting' | 'closed') => {
    setFilter({
      ...filter,
      status
    });
  };

  const formatTime = (date: Date) => {
    return date.toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' });
  };

  const formatDate = (date: Date) => {
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    
    const chatDate = new Date(date);
    chatDate.setHours(0, 0, 0, 0);
    
    if (chatDate.getTime() === today.getTime()) {
      return 'Hoje';
    }
    
    const yesterday = new Date(today);
    yesterday.setDate(yesterday.getDate() - 1);
    
    if (chatDate.getTime() === yesterday.getTime()) {
      return 'Ontem';
    }
    
    return date.toLocaleDateString('pt-BR');
  };

  const openChat = (chat: Chat) => {
    // Aqui seria o código para abrir o chat selecionado
    // e possivelmente marcar mensagens como lidas
    setSelectedChat(chat);
    
    // Atualizar o objeto para marcar como lido
    const updatedChats = chats.map(c => {
      if (c.id === chat.id) {
        return {...c, unreadCount: 0};
      }
      return c;
    });
    
    setChats(updatedChats);
  };

  const getStatusClass = (status: string) => {
    switch (status) {
      case 'active':
        return 'bg-green-100 text-green-800';
      case 'waiting':
        return 'bg-yellow-100 text-yellow-800';
      case 'closed':
        return 'bg-gray-100 text-gray-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  };

  const getStatusLabel = (status: string) => {
    switch (status) {
      case 'active':
        return 'Ativo';
      case 'waiting':
        return 'Aguardando';
      case 'closed':
        return 'Finalizado';
      default:
        return status;
    }
  };

  return (
    <div className="flex h-screen bg-gray-100">
      {/* Sidebar */}
      <div className="w-48 bg-gray-800 text-white flex flex-col">
        <div className="p-4 font-bold">
          Home
        </div>
        <div className="p-4 font-bold bg-teal-500">
          Chamados
        </div>
        <div className="p-4 font-bold">
          Chat Atendente
        </div>
        <div className="mt-auto p-4 text-sm">
          Atendente: Carlos Gomes
        </div>
      </div>

      {/* Main Content */}
      <div className="flex-1 flex flex-col">
        <div className="bg-white p-4 shadow-md">
          <h1 className="text-xl font-bold">Dashboard de Atendimentos</h1>
        </div>

        <div className="flex flex-1 overflow-hidden">
          {/* Chats List Panel */}
          <div className="w-96 border-r border-gray-200 flex flex-col bg-white">
            {/* Search and Filters */}
            <div className="p-4 border-b border-gray-200">
              <input
                type="text"
                placeholder="Buscar por cliente ou número do chamado..."
                className="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-teal-500"
                value={filter.searchTerm}
                onChange={handleSearch}
              />
              
              <div className="flex mt-3 space-x-2">
                <button 
                  onClick={() => handleStatusFilter('all')}
                  className={`px-3 py-1 rounded-md ${filter.status === 'all' ? 'bg-teal-500 text-white' : 'bg-gray-200'}`}
                >
                  Todos
                </button>
                <button 
                  onClick={() => handleStatusFilter('active')}
                  className={`px-3 py-1 rounded-md ${filter.status === 'active' ? 'bg-teal-500 text-white' : 'bg-gray-200'}`}
                >
                  Ativos
                </button>
                <button 
                  onClick={() => handleStatusFilter('waiting')}
                  className={`px-3 py-1 rounded-md ${filter.status === 'waiting' ? 'bg-teal-500 text-white' : 'bg-gray-200'}`}
                >
                  Aguardando
                </button>
                <button 
                  onClick={() => handleStatusFilter('closed')}
                  className={`px-3 py-1 rounded-md ${filter.status === 'closed' ? 'bg-teal-500 text-white' : 'bg-gray-200'}`}
                >
                  Finalizados
                </button>
              </div>
            </div>
            
            {/* Chats List */}
            <div className="flex-1 overflow-y-auto">
              {filteredChats.map(chat => (
                <div 
                  key={chat.id}
                  onClick={() => openChat(chat)}
                  className={`p-4 border-b border-gray-200 cursor-pointer hover:bg-gray-50 ${
                    selectedChat?.id === chat.id ? 'bg-gray-100' : ''
                  } ${
                    chat.unreadCount > 0 ? 'border-l-4 border-l-teal-500' : ''
                  }`}
                >
                  <div className="flex justify-between items-start">
                    <div>
                      <h3 className="font-medium text-gray-900">{chat.clientName}</h3>
                      <p className="text-sm text-gray-500">Chamado #{chat.ticketId}</p>
                    </div>
                    <div className="text-xs text-gray-500">
                      {formatDate(chat.lastMessageTime)}
                      <br />
                      {formatTime(chat.lastMessageTime)}
                    </div>
                  </div>
                  
                  <div className="mt-2 flex justify-between items-center">
                    <p className="text-sm text-gray-600 truncate w-48">{chat.lastMessage}</p>
                    <div className="flex space-x-2">
                      {chat.unreadCount > 0 && (
                        <span className="bg-teal-500 text-white rounded-full w-5 h-5 flex items-center justify-center text-xs">
                          {chat.unreadCount}
                        </span>
                      )}
                      <span className={`text-xs px-2 py-1 rounded-full ${getStatusClass(chat.status)}`}>
                        {getStatusLabel(chat.status)}
                      </span>
                    </div>
                  </div>
                </div>
              ))}
              
              {filteredChats.length === 0 && (
                <div className="p-8 text-center text-gray-500">
                  Nenhum chat corresponde aos filtros aplicados.
                </div>
              )}
            </div>
          </div>
          
          {/* Chat View Panel */}
          <div className="flex-1 flex flex-col bg-gray-50">
            {selectedChat ? (
              <>
                {/* Chat Header */}
                <div className="bg-white p-4 shadow-sm flex justify-between items-center">
                  <div>
                    <h2 className="font-bold">{selectedChat.clientName}</h2>
                    <div className="flex space-x-3 text-sm">
                      <p className="text-gray-600">Chamado #{selectedChat.ticketId}</p>
                      <span className={`px-2 py-0.5 rounded-full ${getStatusClass(selectedChat.status)}`}>
                        {getStatusLabel(selectedChat.status)}
                      </span>
                    </div>
                  </div>
                  <div className="flex space-x-2">
                    <button className="bg-gray-200 hover:bg-gray-300 px-3 py-1 rounded-md text-sm">
                      Histórico
                    </button>
                    {selectedChat.status !== 'closed' && (
                      <button className="bg-red-500 hover:bg-red-600 text-white px-3 py-1 rounded-md text-sm">
                        Finalizar
                      </button>
                    )}
                  </div>
                </div>
                
                {/* Placeholder for chat interface */}
                <div className="flex-1 p-4 flex flex-col justify-center items-center text-gray-500">
                  <p className="text-lg">Chat com {selectedChat.clientName} selecionado</p>
                  <p className="mt-2">Interface de chat carregaria aqui</p>
                  <button className="mt-4 bg-teal-500 text-white px-4 py-2 rounded-md hover:bg-teal-600">
                    Abrir Chat Completo
                  </button>
                </div>
              </>
            ) : (
              <div className="flex-1 flex flex-col justify-center items-center text-gray-500">
                <p className="text-lg">Selecione um chat para visualizar</p>
                <p className="mt-2">Nenhum chat selecionado</p>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default ChatDashboard;