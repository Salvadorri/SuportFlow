import React, { useState, useEffect, useRef } from 'react';

interface Message {
  id: number;
  sender: 'user' | 'attendant';
  text: string;
  timestamp: Date;
}

interface Ticket {
  id: number;
  subject: string;
  status: 'open' | 'in-progress' | 'resolved';
  createdAt: Date;
}

const ChatAtendente: React.FC = () => {
  const [messages, setMessages] = useState<Message[]>([
    {
      id: 1,
      sender: 'user',
      text: 'Olá, preciso de ajuda com meu pedido',
      timestamp: new Date(),
    },
  ]);
  const [inputMessage, setInputMessage] = useState('');
  const [activeTickets, setActiveTickets] = useState<Ticket[]>([]);
  const messagesEndRef = useRef<null | HTMLDivElement>(null);

  useEffect(() => {
    // Exemplo de chamada a API para carregar mensagens anteriores
    // fetchPreviousMessages();
    
    // Exemplo de chamada a API para carregar tickets ativos
    // fetchActiveTickets();
  }, []);

  useEffect(() => {
    scrollToBottom();
  }, [messages]);

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  };

  const handleSendMessage = () => {
    if (inputMessage.trim() === '') return;
    
    const newMessage: Message = {
      id: messages.length + 1,
      sender: 'attendant',
      text: inputMessage,
      timestamp: new Date(),
    };
    
    setMessages([...messages, newMessage]);
    setInputMessage('');
    
    // Simulação de resposta automática após 1 segundo
    setTimeout(() => {
      const autoResponse: Message = {
        id: messages.length + 2,
        sender: 'user',
        text: 'Obrigado pela resposta!',
        timestamp: new Date(),
      };
      setMessages(prev => [...prev, autoResponse]);
    }, 1000);
  };

  const createNewTicket = () => {
    const newTicket: Ticket = {
      id: activeTickets.length + 1,
      subject: 'Novo chamado',
      status: 'open',
      createdAt: new Date(),
    };
    
    setActiveTickets([...activeTickets, newTicket]);
    
    // Adiciona mensagem informando sobre o novo chamado
    const newMessage: Message = {
      id: messages.length + 1,
      sender: 'attendant',
      text: `Chamado #${newTicket.id} aberto com sucesso.`,
      timestamp: new Date(),
    };
    
    setMessages([...messages, newMessage]);
  };

  const formatTime = (date: Date) => {
    return `${date.getHours()}:${date.getMinutes().toString().padStart(2, '0')}`;
  };

  return (
    <div className="flex h-screen bg-gray-100">
      {/* Sidebar */}
      <div className="w-48 bg-gray-800 text-white flex flex-col">
        <div className="p-4 font-bold">
          Home
        </div>
        <div className="p-4 font-bold">
          Chamados
        </div>
        <div className="p-4 font-bold bg-teal-500">
          Chat Atendente
        </div>
        <div className="mt-auto p-4 text-sm">
          Tickets ativos: {activeTickets.length}
        </div>
      </div>

      {/* Main Content */}
      <div className="flex-1 flex flex-col">
        {/* Chat Header */}
        <div className="bg-white p-4 shadow-md flex justify-between items-center">
          <div>
            <h1 className="font-bold">Chat com Cliente</h1>
            <p className="text-sm text-gray-500">Online - Último acesso: {formatTime(new Date())}</p>
          </div>
          <button 
            onClick={createNewTicket}
            className="bg-teal-500 text-white px-4 py-2 rounded-md hover:bg-teal-600"
          >
            Novo Chamado
          </button>
        </div>

        {/* Messages Container */}
        <div className="flex-1 overflow-y-auto p-4 bg-gray-50">
          {messages.map((message) => (
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
            />
            <button
              onClick={handleSendMessage}
              className="bg-teal-500 text-white px-4 py-2 rounded-r-md hover:bg-teal-600 flex items-center justify-center"
            >
              ➤
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ChatAtendente;