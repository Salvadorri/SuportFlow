import React, { useState, useEffect, useRef } from 'react';
import { Link } from "@tanstack/react-router";
import logo from "../../assets/logo.png";

const menuItems = [
  { label: "Abrir Chamado", href: "/criar-chamado" },
  { label: "Histórico Chamados", href: "/chamados-historico" },
  { label: "Chat", href: "/chatchamadodash" }
];
interface Chat {
  id: number;
  clientName: string;
  ticketId: number;
  lastMessage: string;
  lastMessageTime: Date;
  status: 'active' | 'waiting' | 'closed';
  unreadCount: number;
  messages: Message[];
}

interface Message {
  id: number;
  sender: 'user' | 'attendant';
  text: string;
  timestamp: Date;
}

interface ChatFilter {
  searchTerm: string;
  status: 'all' | 'active' | 'waiting' | 'closed';
}

const IntegratedChatDashboard: React.FC = () => {
  const [chats, setChats] = useState<Chat[]>([]);
  const [filteredChats, setFilteredChats] = useState<Chat[]>([]);
  const [selectedChat, setSelectedChat] = useState<Chat | null>(null);
  const [inputMessage, setInputMessage] = useState('');
  const [filter, setFilter] = useState<ChatFilter>({
    searchTerm: '',
    status: 'all'
  });
  const messagesEndRef = useRef<null | HTMLDivElement>(null);

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
        unreadCount: 2,
        messages: [
          {
            id: 1,
            sender: 'user',
            text: 'Olá, estou com problemas para acessar minha conta.',
            timestamp: new Date(2025, 1, 18, 14, 30)
          },
          {
            id: 2,
            sender: 'attendant',
            text: 'Olá Maria, vou te ajudar com isso. Você já tentou redefinir sua senha?',
            timestamp: new Date(2025, 1, 18, 14, 32)
          },
          {
            id: 3,
            sender: 'user',
            text: 'Sim, tentei mas não recebi o e-mail de recuperação.',
            timestamp: new Date(2025, 1, 18, 14, 33)
          },
          {
            id: 4,
            sender: 'attendant',
            text: 'Entendi. Vou verificar o que está acontecendo com o envio de e-mails para o seu endereço.',
            timestamp: new Date(2025, 1, 18, 14, 34)
          },
          {
            id: 5,
            sender: 'user',
            text: 'Obrigado pela ajuda!',
            timestamp: new Date(2025, 1, 18, 14, 35)
          }
        ]
      },
      {
        id: 2,
        clientName: 'João Oliveira',
        ticketId: 5433,
        lastMessage: 'Ainda estou aguardando a solução...',
        lastMessageTime: new Date(2025, 1, 19, 9, 12),
        status: 'waiting',
        unreadCount: 0,
        messages: [
          {
            id: 1,
            sender: 'user',
            text: 'Bom dia, meu sistema está apresentando lentidão após a última atualização.',
            timestamp: new Date(2025, 1, 19, 9, 5)
          },
          {
            id: 2,
            sender: 'attendant',
            text: 'Bom dia João, vamos verificar isso. Você poderia me informar qual versão do sistema está utilizando?',
            timestamp: new Date(2025, 1, 19, 9, 8)
          },
          {
            id: 3,
            sender: 'user',
            text: 'Estou usando a versão 2.3.5, atualizada ontem.',
            timestamp: new Date(2025, 1, 19, 9, 10)
          },
          {
            id: 4,
            sender: 'user',
            text: 'Ainda estou aguardando a solução...',
            timestamp: new Date(2025, 1, 19, 9, 12)
          }
        ]
      },
      {
        id: 3,
        clientName: 'Ana Pereira',
        ticketId: 5430,
        lastMessage: 'Problema resolvido, muito obrigada.',
        lastMessageTime: new Date(2025, 1, 17, 16, 45),
        status: 'closed',
        unreadCount: 0,
        messages: [
          {
            id: 1,
            sender: 'user',
            text: 'Boa tarde, preciso cancelar minha assinatura.',
            timestamp: new Date(2025, 1, 17, 16, 30)
          },
          {
            id: 2,
            sender: 'attendant',
            text: 'Olá Ana, posso ajudar com isso. Para confirmar, você gostaria de cancelar a assinatura do plano Premium, correto?',
            timestamp: new Date(2025, 1, 17, 16, 35)
          },
          {
            id: 3,
            sender: 'user',
            text: 'Sim, exatamente.',
            timestamp: new Date(2025, 1, 17, 16, 37)
          },
          {
            id: 4,
            sender: 'attendant',
            text: 'Pronto, Ana. Acabei de processar o cancelamento. Você receberá um e-mail de confirmação em breve.',
            timestamp: new Date(2025, 1, 17, 16, 42)
          },
          {
            id: 5,
            sender: 'user',
            text: 'Problema resolvido, muito obrigada.',
            timestamp: new Date(2025, 1, 17, 16, 45)
          }
        ]
      },
      {
        id: 4,
        clientName: 'Carlos Santos',
        ticketId: 5435,
        lastMessage: 'Preciso de suporte urgente para configurar o sistema.',
        lastMessageTime: new Date(2025, 1, 19, 10, 23),
        status: 'active',
        unreadCount: 3,
        messages: [
          {
            id: 1,
            sender: 'user',
            text: 'Preciso de suporte urgente para configurar o sistema.',
            timestamp: new Date(2025, 1, 19, 10, 23)
          }
        ]
      },
      {
        id: 5,
        clientName: 'Luiza Mendes',
        ticketId: 5429,
        lastMessage: 'Vou verificar se a solução funcionou e retorno.',
        lastMessageTime: new Date(2025, 1, 18, 11, 17),
        status: 'waiting',
        unreadCount: 0,
        messages: [
          {
            id: 1,
            sender: 'user',
            text: 'Estou com problemas para gerar relatórios no módulo financeiro.',
            timestamp: new Date(2025, 1, 18, 11, 5)
          },
          {
            id: 2,
            sender: 'attendant',
            text: 'Olá Luiza, vamos verificar isso. Você poderia me enviar um print do erro que aparece?',
            timestamp: new Date(2025, 1, 18, 11, 8)
          },
          {
            id: 3,
            sender: 'user',
            text: 'Claro, aqui está o erro [imagem]',
            timestamp: new Date(2025, 1, 18, 11, 12)
          },
          {
            id: 4,
            sender: 'attendant',
            text: 'Obrigado, Luiza. Identifiquei o problema. Tente limpar o cache do navegador e tentar novamente.',
            timestamp: new Date(2025, 1, 18, 11, 15)
          },
          {
            id: 5,
            sender: 'user',
            text: 'Vou verificar se a solução funcionou e retorno.',
            timestamp: new Date(2025, 1, 18, 11, 17)
          }
        ]
      }
    ];
    
    setChats(mockChats);
    setFilteredChats(mockChats);
  }, []);

  useEffect(() => {
    applyFilters();
  }, [filter, chats]);

  useEffect(() => {
    scrollToBottom();
  }, [selectedChat]);

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  };

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

  const handleSendMessage = () => {
    if (!selectedChat || inputMessage.trim() === '') return;
    
    const newMessage: Message = {
      id: selectedChat.messages.length + 1,
      sender: 'attendant',
      text: inputMessage,
      timestamp: new Date(),
    };
    
    // Atualizar as mensagens do chat atual
    const updatedChats = chats.map(chat => {
      if (chat.id === selectedChat.id) {
        const updatedChat = {
          ...chat,
          messages: [...chat.messages, newMessage],
          lastMessage: inputMessage,
          lastMessageTime: new Date(),
          status: chat.status === 'closed' ? 'active' : chat.status
        };
        
        setSelectedChat(updatedChat);
        return updatedChat;
      }
      return chat;
    });
    
    setChats(updatedChats);
    setInputMessage('');
  };

  const closeTicket = (chatId: number) => {
    const updatedChats = chats.map(chat => {
      if (chat.id === chatId) {
        const updatedChat = {
          ...chat,
          status: 'closed'
        };
        
        setSelectedChat(updatedChat);
        return updatedChat;
      }
      return chat;
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
      <aside className="w-64 bg-gray-900 text-white p-5">
        <div className="flex items-center mb-6">
          <img src={logo} alt="SupportFlow Logo" className="h-14 w-auto mr-2"/>
          <h1 className="text-2xl font-bold">SupportFlow</h1>
        </div>
        <nav>
          <ul>
            {menuItems.map((item) => (
              <li
                key={item.label}
                className="mb-2 hover:bg-green-700 p-2 rounded"
              >
                <Link to={item.href} className="text-white hover:text-white block w-full h-full">
                  {item.label}
                </Link>
              </li>
            ))}
          </ul>
        </nav>
      </aside>

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
                      <button 
                        onClick={() => closeTicket(selectedChat.id)}
                        className="bg-red-500 hover:bg-red-600 text-white px-3 py-1 rounded-md text-sm"
                      >
                        Finalizar
                      </button>
                    )}
                  </div>
                </div>
                
                {/* Chat Messages */}
                <div className="flex-1 overflow-y-auto p-4 bg-gray-50">
                  {selectedChat.messages.map((message) => (
                    <div 
                      key={message.id} 
                      className={`mb-4 flex ${message.sender === 'attendant' ? 'justify-end' : 'justify-start'}`}
                    >
                      <div 
                        className={`max-w-xs md:max-w-md rounded-lg p-3 ${
                          message.sender === 'attendant' 
                            ? 'bg-teal-500 text-white' 
                            : 'bg-white border border-gray-200'
                        }`}
                      >
                        <p>{message.text}</p>
                        <p className={`text-xs mt-1 text-right ${
                          message.sender === 'attendant' ? 'text-teal-100' : 'text-gray-500'
                        }`}>
                          {formatTime(message.timestamp)}
                        </p>
                      </div>
                    </div>
                  ))}
                  <div ref={messagesEndRef} />
                </div>

                {/* Input Area */}
                <div className="bg-white p-4 border-t border-gray-200">
                  <div className="flex">
                    <button className="px-3 text-gray-500 flex items-center justify-center border border-gray-300 border-r-0 rounded-l-md hover:bg-gray-100">
                      📎
                    </button>
                    <input
                      type="text"
                      value={inputMessage}
                      onChange={(e) => setInputMessage(e.target.value)}
                      onKeyPress={(e) => e.key === 'Enter' && handleSendMessage()}
                      placeholder="Digite sua mensagem..."
                      className="flex-1 border border-gray-300 px-4 py-2 focus:outline-none focus:ring-2 focus:ring-teal-500"
                      disabled={selectedChat.status === 'closed'}
                    />
                    <button
                      onClick={handleSendMessage}
                      className={`text-white px-4 py-2 rounded-r-md flex items-center justify-center ${
                        selectedChat.status === 'closed' 
                          ? 'bg-gray-400 cursor-not-allowed' 
                          : 'bg-teal-500 hover:bg-teal-600'
                      }`}
                      disabled={selectedChat.status === 'closed'}
                    >
                      ➤
                    </button>
                  </div>
                  {selectedChat.status === 'closed' && (
                    <p className="text-sm text-gray-500 mt-2 text-center">
                      Este chamado está finalizado. Não é possível enviar novas mensagens.
                    </p>
                  )}
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

export default IntegratedChatDashboard;