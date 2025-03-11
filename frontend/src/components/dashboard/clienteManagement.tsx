// src/components/dashboard/ClientManagement.tsx
import React, { useEffect, useState } from "react";
import {
  createClient,
  deleteClientById,
  getAllClients,
  getClientById,
  updateClient,
} from "../../api/clienteApi";
import ClientForm from "./clienteForm";

interface Client {
  id: string;
  nome: string;
  email: string;
  telefone?: string;
  cpfCnpj?: string;
}

interface ClientManagementProps {
  showMessage: (message: string, type: "success" | "error") => void;
  displayMode: "create" | "list";
}

const ClientManagement: React.FC<ClientManagementProps> = ({
  showMessage,
  displayMode,
}) => {
  const [clients, setClients] = useState<Client[]>([]);
  const [selectedClient, setSelectedClient] = useState<Client | null>(null);
  const [editMode, setEditMode] = useState(false);
  const [searchTerm, setSearchTerm] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [phone, setPhone] = useState("");
  const [cpfCnpj, setCpfCnpj] = useState("");
  const [senha, setSenha] = useState("");

  useEffect(() => {
    fetchAllClients();
  }, []);

  const fetchAllClients = async () => {
    setIsLoading(true);
    try {
      const allClients = await getAllClients();
      setClients(allClients || []);
    } catch (error: any) {
      showMessage(`Erro ao carregar clientes: ${error.message}`, "error");
    } finally {
      setIsLoading(false);
    }
  };

  const resetForm = () => {
    setName("");
    setEmail("");
    setPhone("");
    setCpfCnpj("");
    setSenha("");
  };
  const handleCreateClient = async () => {
    if (!validateForm()) return;

    setIsLoading(true);
    try {
      const newClient = {
        nome: name.trim(),
        email: email.trim(),
        telefone: phone.trim() || undefined,
        cpfCnpj: cpfCnpj.trim() || undefined,
        senha: senha,
      };
      await createClient(newClient);
      showMessage(`Cliente criado com sucesso!`, "success");
      resetForm();
      fetchAllClients();
    } catch (error: any) {
      showMessage(`Erro ao criar cliente: ${error.message}`, "error");
    } finally {
      setIsLoading(false);
    }
  };

  const handleUpdateClient = async () => {
    if (!selectedClient || !validateForm(true)) return;

    setIsLoading(true);
    try {
      const updatedClientData = {
        nome: name.trim(),
        email: email.trim(),
        telefone: phone.trim() || undefined,
        cpfCnpj: cpfCnpj.trim() || undefined,
      };

      await updateClient(selectedClient.id, updatedClientData);
      showMessage(`Cliente atualizado com sucesso!`, "success");
      setSelectedClient(null);
      setEditMode(false);
      resetForm();
      fetchAllClients();
    } catch (error: any) {
      showMessage(`Erro ao atualizar cliente: ${error.message}`, "error");
    } finally {
      setIsLoading(false);
    }
  };

  const handleDeleteClient = async (clientId: string) => {
    if (!confirm("Tem certeza que deseja excluir este cliente?")) return;

    setIsLoading(true);
    try {
      await deleteClientById(clientId);
      showMessage(`Cliente excluído com sucesso!`, "success");
      setSelectedClient(null);
      fetchAllClients();
    } catch (error: any) {
      showMessage(`Erro ao excluir cliente: ${error.message}`, "error");
    } finally {
      setIsLoading(false);
    }
  };

  const handleSelectClient = async (clientId: string) => {
    setIsLoading(true);
    try {
      const client = await getClientById(clientId);
      setSelectedClient(client);
      setName(client.nome || "");
      setEmail(client.email || "");
      setPhone(client.telefone || "");
      setCpfCnpj(client.cpfCnpj || "");
      setEditMode(true);
    } catch (error: any) {
      showMessage(
        `Erro ao buscar detalhes do cliente: ${error.message}`,
        "error"
      );
    } finally {
      setIsLoading(false);
    }
  };

  const handleCancelEdit = () => {
    setSelectedClient(null);
    setEditMode(false);
    resetForm();
  };

  const validateForm = (isEdit = false) => {
    if (!name.trim()) {
      showMessage("Nome é obrigatório", "error");
      return false;
    }
    if (!email.trim() || !/\S+@\S+\.\S+/.test(email)) {
      showMessage("Email inválido", "error");
      return false;
    }
    if (!isEdit && !senha) {
      showMessage("Senha é obrigatória para novos clientes", "error");
      return false;
    }
    return true;
  };

  const filteredClients = clients.filter(
    (client) =>
      client.nome?.toLowerCase().includes(searchTerm.toLowerCase()) ||
      client.email?.toLowerCase().includes(searchTerm.toLowerCase()) ||
      client.cpfCnpj?.includes(searchTerm)
  );

  return (
    <div>
      {displayMode === "create" && (
        <div className="mb-8">
          <h2 className="text-2xl font-semibold mb-4">Criar Cliente</h2>
          <div className="bg-white p-6 rounded-lg shadow-md">
            <ClientForm
              name={name}
              setName={setName}
              email={email}
              setEmail={setEmail}
              phone={phone}
              setPhone={setPhone}
              cpfCnpj={cpfCnpj}
              setCpfCnpj={setCpfCnpj}
              senha={senha}
              setSenha={setSenha}
              isLoading={isLoading}
              handleSubmit={handleCreateClient}
            />
          </div>
        </div>
      )}

      {displayMode === "list" && (
        <div>
          <div className="flex justify-between items-center mb-4">
            <h2 className="text-2xl font-semibold">Lista de Clientes</h2>
            <input
              type="text"
              placeholder="Pesquisar clientes..."
              className="p-2 border rounded-lg focus:ring-2 focus:ring-blue-500"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
            />
          </div>

          <div className="bg-white shadow-lg rounded-lg overflow-hidden">
            {isLoading && (
              <div className="p-4 text-center text-gray-500">
                <div className="animate-pulse flex justify-center">
                  <div className="h-5 w-24 bg-gray-200 rounded"></div>
                </div>
                <p className="mt-2">Carregando clientes...</p>
              </div>
            )}

            {!isLoading && (
              <table className="w-full border-collapse">
                <thead>
                  <tr className="bg-gray-100 text-left border-b border-gray-200">
                    <th className="p-4 font-semibold text-gray-700">Nome</th>
                    <th className="p-4 font-semibold text-gray-700">Email</th>
                    <th className="p-4 font-semibold text-gray-700">
                      Telefone
                    </th>
                    <th className="p-4 font-semibold text-gray-700">
                      CPF/CNPJ
                    </th>
                    <th className="p-4 font-semibold text-gray-700">Ações</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredClients.length > 0 ? (
                    filteredClients.map((client) => (
                      <tr
                        key={client.id}
                        className="border-b hover:bg-gray-50 transition-colors"
                      >
                        <td className="p-4">{client.nome}</td>
                        <td className="p-4">{client.email}</td>
                        <td className="p-4">{client.telefone}</td>
                        <td className="p-4">{client.cpfCnpj}</td>
                        <td className="p-4">
                          <div className="flex space-x-3">
                            <button
                              onClick={() => handleSelectClient(client.id)}
                              className="text-blue-600 hover:text-blue-800 font-medium transition-colors"
                            >
                              Editar
                            </button>
                            <button
                              onClick={() => handleDeleteClient(client.id)}
                              className="text-red-600 hover:text-red-800 font-medium transition-colors"
                            >
                              Excluir
                            </button>
                          </div>
                        </td>
                      </tr>
                    ))
                  ) : (
                    <tr>
                      <td colSpan={5} className="p-6 text-center text-gray-500">
                        {clients.length === 0 ? (
                          <div>
                            <p className="text-lg mb-2">
                              Nenhum cliente encontrado
                            </p>
                            <p className="text-sm">
                              Adicione um novo cliente usando o formulário
                              acima.
                            </p>
                          </div>
                        ) : (
                          "Nenhum cliente corresponde à pesquisa."
                        )}
                      </td>
                    </tr>
                  )}
                </tbody>
              </table>
            )}
          </div>
        </div>
      )}

      {editMode && (
        <div className="mb-8">
          <h2 className="text-2xl font-semibold mb-4">Editar Cliente</h2>
          <div className="bg-white p-6 rounded-lg shadow-md">
            <ClientForm
              name={name}
              setName={setName}
              email={email}
              setEmail={setEmail}
              phone={phone}
              setPhone={setPhone}
              cpfCnpj={cpfCnpj}
              setCpfCnpj={setCpfCnpj}
              isLoading={isLoading}
              handleSubmit={handleUpdateClient}
              handleCancelEdit={handleCancelEdit}
              isEdit={true}
            />
          </div>
        </div>
      )}
    </div>
  );
};

export default ClientManagement;
