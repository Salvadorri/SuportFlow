import React, { useState, useEffect, useMemo } from "react";
import { Chart as ChartJS, ArcElement, Tooltip, Legend, CategoryScale, LinearScale, BarElement, Title } from 'chart.js';
import { Pie, Bar } from 'react-chartjs-2';
import DatePicker from 'react-datepicker';
import "react-datepicker/dist/react-datepicker.css";
import logo from "../../assets/logo.png";
import { Link } from "@tanstack/react-router";

interface Ticket {
  id: number;
  title: string;
  status: string;
  priority: string;
  date: string;
  color: string;
}

const Chamados: React.FC = () => {
  const initialTickets: Ticket[] = [
    {
      id: 101,
      title: "Erro no login",
      status: "Em andamento",
      priority: "Alta",
      color: "text-yellow-500",
      date: "2025-02-06",
    },
    {
      id: 102,
      title: "Problema de pagamento",
      status: "Resolvido",
      priority: "Média",
      color: "text-green-500",
      date: "2025-02-05",
    },
    {
      id: 103,
      title: "Erro na integração",
      status: "Em andamento",
      priority: "Alta",
      color: "text-yellow-500",
      date: "2025-02-06",
    },
  ];

  const [tickets, setTickets] = useState<Ticket[]>(initialTickets);
  const [sortConfig, setSortConfig] = useState<{ key: keyof Ticket | null; direction: 'asc' | 'desc' }>({ key: 'date', direction: 'desc' });
  const [startDate, setStartDate] = useState<Date | null>(null);
  const [endDate, setEndDate] = useState<Date | null>(null);
  const [filterType, setFilterType] = useState<'dateRange' | 'day' | 'week' | 'month' | 'year'>('dateRange');

  const chartWidth = 800;
  const chartHeight = 600;

  const menuItems = [
    { label: "Abrir Chamado", href: "/criar-chamado" },
    { label: "Histórico Chamados", href: "/chamados-historico" },
    { label: "Chat", href: "/chatchamadodash" }
  ];

  const cardItems = [
    {
      title: "Novos Chamados",
      description: "Gerencie os novos tickets",
      buttonText: "+ Criar Novo Chamado",
      href: "/criar-chamado"
    },
    {
      title: "Em Andamento",
      description: "Acompanhe chamados ativos",
      buttonText: "Ver Em Andamento",
      href: "#"
    },
    {
      title: "Histórico",
      description: "Visualize chamados fechados",
      buttonText: "Ver Histórico",
      href: "/chamados-historico"
    },
  ];

  const statItems = [
    { label: "Chamados Abertos", value: 24 },
    { label: "Em Andamento", value: 16 },
    { label: "Resolvidos Hoje", value: 8 },
  ];

  const sortTickets = (key: keyof Ticket) => {
    let direction: 'asc' | 'desc' = "asc";
    if (sortConfig.key === key && sortConfig.direction === "asc") {
      direction = "desc";
    }
    setSortConfig({ key, direction });

    const sortedTickets = [...tickets].sort((a, b) => {
      if (key === 'priority') {
        const priorityOrder: { [key: string]: number } = { 'Alta': 3, 'Média': 2, 'Baixa': 1 };
        return direction === 'asc'
          ? priorityOrder[a.priority] - priorityOrder[b.priority]
          : priorityOrder[b.priority] - priorityOrder[a.priority];
      } else if (key === 'date') {
        const dateA = new Date(a[key]);
        const dateB = new Date(b[key]);
        return direction === 'asc' ? dateA.getTime() - dateB.getTime() : dateB.getTime() - dateA.getTime();
      } else {
        const aValue = a[key];
        const bValue = b[key];

        if (typeof aValue === 'string' && typeof bValue === 'string') {
          return direction === 'asc'
            ? aValue.localeCompare(bValue)
            : bValue.localeCompare(aValue);
        }
      }
      return 0;
    });

    setTickets(sortedTickets);
  };

  ChartJS.register(ArcElement, Tooltip, Legend, CategoryScale, LinearScale, BarElement, Title);

  useEffect(() => {
    const mockTickets: Ticket[] = [
      { id: 1, title: 'Problema de rede', status: 'Em andamento', priority: 'Alta', date: '2023-11-15', color: 'text-yellow-500' },
      { id: 2, title: 'Erro no servidor', status: 'Resolvido', priority: 'Média', date: '2023-11-10', color: 'text-green-500' },
      { id: 3, title: 'Computador lento', status: 'Em andamento', priority: 'Baixa', date: '2023-11-20', color: 'text-yellow-500' },
      { id: 4, title: 'Falha na impressora', status: 'Finalizado', priority: 'Alta', date: '2023-11-05', color: 'text-gray-500' },
      { id: 5, title: 'Software não abre', status: 'Resolvido', priority: 'Média', date: '2023-11-18', color: 'text-green-500' },
      { id: 6, title: 'Problema de login', status: 'Em andamento', priority: 'Alta', date: '2023-11-22', color: 'text-yellow-500' },
      { id: 7, title: 'Monitor não liga', status: 'Finalizado', priority: 'Média', date: '2023-11-01', color: 'text-gray-500' },
    ];
    setTickets(mockTickets);
  }, []);

  const filteredTickets = useMemo(() => {
    if (!startDate || !endDate) {
      return tickets;
    }

    return tickets.filter(ticket => {
      const ticketDate = new Date(ticket.date);
      return ticketDate >= startDate && ticketDate <= endDate;
    });
  }, [tickets, startDate, endDate]);

  const calculateChartData = (ticketsToCount: Ticket[]) => {
    let emAndamento = 0;
    let resolvidos = 0;
    let finalizados = 0;

    ticketsToCount.forEach(ticket => {
      if (ticket.status === 'Em andamento') {
        emAndamento++;
      } else if (ticket.status === 'Resolvido') {
        resolvidos++;
      } else if (ticket.status === 'Finalizado') {
        finalizados++;
      }
    });

    return { emAndamento, resolvidos, finalizados };
  };

  const pieChartData = useMemo(() => {
    const { emAndamento, resolvidos, finalizados } = calculateChartData(tickets);
    return {
      labels: ['Alta', 'Média', 'Baixa'],
      datasets: [
        {
          data: [emAndamento, resolvidos, finalizados],
          backgroundColor: ['#FFCE56', '#36A2EB', '#999999'],
        },
      ],
    };
  }, [tickets]);

  const barChartData = useMemo(() => {
    const { emAndamento, resolvidos } = calculateChartData(filteredTickets);
    return {
      labels: ['Em Andamento', 'Resolvidos'],
      datasets: [
        {
          label: '', // Removendo a label aqui
          data: [emAndamento, resolvidos],
          backgroundColor: ['#FFCE56', '#36A2EB', '#999999'],
        },
      ],
    };
  }, [filteredTickets]);

  const barChartOptions = {
    plugins: {
      legend: {
        display: false, // Removendo a legenda
      },
    },
  };

  const handleFilterTypeChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
    setFilterType(event.target.value as 'dateRange' | 'day' | 'week' | 'month' | 'year');

    const now = new Date();
    switch (event.target.value) {
      case 'day':
        setStartDate(new Date(now.getFullYear(), now.getMonth(), now.getDate()));
        setEndDate(new Date(now.getFullYear(), now.getMonth(), now.getDate(), 23, 59, 59));
        break;
      case 'week':
        const firstDayOfWeek = new Date(now.setDate(now.getDate() - now.getDay()));
        const lastDayOfWeek = new Date(now.setDate(now.getDate() - now.getDay() + 6));
        setStartDate(firstDayOfWeek);
        setEndDate(lastDayOfWeek);
        break;
      case 'month':
        setStartDate(new Date(now.getFullYear(), now.getMonth(), 1));
        setEndDate(new Date(now.getFullYear(), now.getMonth() + 1, 0));
        break;
      case 'year':
        setStartDate(new Date(now.getFullYear(), 0, 1));
        setEndDate(new Date(now.getFullYear(), 11, 31));
        break;
      case 'dateRange':
        setStartDate(null);
        setEndDate(null);
        break;
      default:
        setStartDate(null);
        setEndDate(null);
    }
  };

  return (
    <div className="flex h-screen overflow-hidden">
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

      <main className="flex-1 p-6 overflow-auto">
        <h2 className="text-3xl font-semibold mb-6">Dashboard de Chamados</h2>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-6">
          {cardItems.map((card) => (
            <div key={card.title} className="bg-white p-6 rounded-lg shadow">
              <h3 className="text-lg font-semibold">{card.title}</h3>
              <p className="text-gray-600 mb-4">{card.description}</p>
              <Link to={card.href}>
                <button className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700">
                  {card.buttonText}
                </button>
              </Link>
            </div>
          ))}
        </div>

        <div className="bg-white p-6 rounded-lg shadow mb-6">
          <h3 className="text-lg font-semibold mb-4">
            Estatísticas de Chamados
          </h3>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            {statItems.map((stat) => (
              <div key={stat.label} className="p-4 bg-gray-100 rounded">
                <h4 className="text-gray-600">{stat.label}</h4>
                <p className="text-2xl font-bold">{stat.value}</p>
              </div>
            ))}
          </div>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div className="bg-white p-6 rounded-lg shadow flex flex-col items-center justify-center h-full">
            <h3 className="text-lg font-semibold mb-4">Status de Prioridade (Tempo Real)</h3>
            <div className="flex items-center justify-center" style={{ width: '500px', height: '500px' }}>
              <Pie data={pieChartData} />
            </div>
          </div>

          <div className="bg-white p-6 rounded-lg shadow flex flex-col items-center justify-center h-full">
            <h3 className="text-lg font-semibold mb-4">Status de Resolução</h3>

            <div className="mb-4 flex flex-wrap items-end">
              <div className="mr-4 mb-2">
                <label htmlFor="filterType" className="block text-sm font-medium text-gray-700">Tipo de Filtro:</label>
                <select
                  id="filterType"
                  value={filterType}
                  onChange={handleFilterTypeChange}
                  className="mt-1 block w-full py-2 px-3 border border-gray-300 bg-white rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                >
                  <option value="dateRange">Intervalo de Datas</option>
                  <option value="day">Dia</option>
                  <option value="week">Semana</option>
                  <option value="month">Mês</option>
                  <option value="year">Ano</option>
                </select>
              </div>

              {filterType === 'dateRange' && (
                <>
                  <div className="mr-4 mb-2">
                    <label htmlFor="startDate" className="block text-sm font-medium text-gray-700">Data Inicial:</label>
                    <DatePicker
                      id="startDate"
                      selected={startDate}
                      onChange={(date: Date | null) => setStartDate(date)}
                      selectsStart
                      startDate={startDate}
                      endDate={endDate}
                      className="mt-1 block w-full py-2 px-3 border border-gray-300 bg-white rounded-md shadow-sm"
                    />
                  </div>

                  <div className="mr-4 mb-2">
                    <label htmlFor="endDate" className="block text-sm font-medium text-gray-700">Data Final:</label>
                    <DatePicker
                      id="endDate"
                      selected={endDate}
                      onChange={(date: Date | null) => setEndDate(date)}
                      selectsEnd
                      startDate={startDate}
                      endDate={endDate}
                      minDate={startDate}
                      className="mt-1 block w-full py-2 px-3 border border-gray-300 bg-white rounded-md shadow-sm"
                    />
                  </div>
                </>
              )}
            </div>

            <div className="flex items-center justify-center flex-grow" style={{ width: `${chartWidth}px`, height: `${chartHeight}px` }}>
              <Bar data={barChartData} options={barChartOptions} />
            </div>
          </div>
        </div>
      </main>
    </div>
  );
}

export default Chamados;