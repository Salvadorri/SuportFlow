import React, { useState, useEffect } from 'react';
import { Link } from "@tanstack/react-router";
import { createUser, updateUser, getAllUsers, getUserById, deleteUserById } from '../../api/userApi';

const UserManagement: React.FC = () => {
  // User form state
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [phone, setPhone] = useState('');
  const [cpfCnpj, setCpfCnpj] = useState('');
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState('');
  const [messageType, setMessageType] = useState<'success' | 'error' | ''>('');
  
  // Users list state
  const [users, setUsers] = useState<any[]>([]);
  const [selectedUser, setSelectedUser] = useState<any>(null);
  const [editMode, setEditMode] = useState(false);
  const [searchTerm, setSearchTerm] = useState('');
  
  // Load all users on component mount
  useEffect(() => {
    fetchAllUsers();
  }, []);
  
  const fetchAllUsers = async () => {
    try {
      const allUsers = await getAllUsers();
      setUsers(allUsers || []);
    } catch (error: any) {
      showMessage(`Erro ao carregar usuários: ${error.message}`, 'error');
    }
  };
  
  const showMessage = (msg: string, type: 'success' | 'error') => {
    setMessage(msg);
    setMessageType(type);
    // Auto-clear message after 5 seconds
    setTimeout(() => {
      setMessage('');
      setMessageType('');
    }, 5000);
  };

  const resetForm = () => {
    setName('');
    setEmail('');
    setPhone('');
    setCpfCnpj('');
    setPassword('');
  };

  const handleCreateUser = async () => {
    try {
      const newUser = {
        nome: name,
        email: email,
        telefone: phone,
        cpfCnpj: cpfCnpj,
        password: password,
        roles: ["USER"] // Example of hardcoded role, can be dynamic
      };
      const createdUser = await createUser(newUser);
      showMessage(`Usuário criado com sucesso!`, 'success');
      resetForm();
      fetchAllUsers();
    } catch (error: any) {
      showMessage(`Erro ao criar usuário: ${error.message}`, 'error');
    }
  };

  const handleUpdateUser = async () => {
    if (!selectedUser) return;
    
    try {
      const updatedUserData = {
        nome: name,
        email: email,
        telefone: phone,
        cpfCnpj: cpfCnpj
      };
      
      const updatedUser = await updateUser(selectedUser.id, updatedUserData);
      showMessage(`Usuário atualizado com sucesso!`, 'success');
      setSelectedUser(null);
      setEditMode(false);
      resetForm();
      fetchAllUsers();
    } catch (error: any) {
      showMessage(`Erro ao atualizar usuário: ${error.message}`, 'error');
    }
  };

  const handleDeleteUser = async (userId: string) => {
    if (!confirm('Tem certeza que deseja excluir este usuário?')) return;
    
    try {
      await deleteUserById(userId);
      showMessage(`Usuário excluído com sucesso!`, 'success');
      setSelectedUser(null);
      fetchAllUsers();
    } catch (error: any) {
      showMessage(`Erro ao excluir usuário: ${error.message}`, 'error');
    }
  };

  const handleSelectUser = async (userId: string) => {
    try {
      const user = await getUserById(userId);
      setSelectedUser(user);
      setName(user.nome || '');
      setEmail(user.email || '');
      setPhone(user.telefone || '');
      setCpfCnpj(user.cpfCnpj || '');
      setEditMode(true);
    } catch (error: any) {
      showMessage(`Erro ao buscar detalhes do usuário: ${error.message}`, 'error');
    }
  };

  const handleCancelEdit = () => {
    setSelectedUser(null);
    setEditMode(false);
    resetForm();
  };

  const filteredUsers = users.filter(user => 
    (user.nome?.toLowerCase().includes(searchTerm.toLowerCase())) ||
    (user.email?.toLowerCase().includes(searchTerm.toLowerCase())) ||
    (user.cpfCnpj?.includes(searchTerm))
  );

  return (
    <div className="flex h-screen">
      {/* Sidebar */}
      <aside className="w-64 bg-gray-900 text-white p-5">
        <h1 className="text-2xl font-bold mb-6">User Management</h1>
        <nav>
          <ul>
            <li className="mb-4 font-semibold">Menu:</li>
            <li className="mb-2 hover:bg-gray-700 p-2 rounded">
              <Link to="/dashboard" className="text-blue-400 block w-full h-full">
                Dashboard
              </Link>
            </li>
            <li className="mb-2 hover:bg-gray-700 p-2 rounded">
              <a href="#" className="text-blue-400">Usuários</a>
            </li>
          </ul>
        </nav>
      </aside>

      {/* Main Content */}
      <main className="flex-1 p-6 bg-gray-100 overflow-auto">
        {/* Message display */}
        {message && (
          <div className={`mb-4 p-3 rounded ${messageType === 'success' ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'}`}>
            {message}
          </div>
        )}

        <div className="mb-8">
          <h2 className="text-2xl font-semibold mb-4">{editMode ? 'Editar Usuário' : 'Criar Usuário'}</h2>
          <div className="bg-white p-6 rounded-lg shadow-md">
            <form className="space-y-4" onSubmit={(e) => {
              e.preventDefault();
              editMode ? handleUpdateUser() : handleCreateUser();
            }}>
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <label className="block text-gray-700 mb-2">Nome *</label>
                  <input 
                    type="text" 
                    className="w-full p-2 border rounded focus:ring-2 focus:ring-blue-500" 
                    placeholder="Nome completo" 
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    required
                  />
                </div>
                <div>
                  <label className="block text-gray-700 mb-2">Email *</label>
                  <input 
                    type="email" 
                    className="w-full p-2 border rounded focus:ring-2 focus:ring-blue-500" 
                    placeholder="Email" 
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    required
                  />
                </div>
              </div>

              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <label className="block text-gray-700 mb-2">Telefone *</label>
                  <input 
                    type="text" 
                    className="w-full p-2 border rounded focus:ring-2 focus:ring-blue-500" 
                    placeholder="Telefone" 
                    value={phone}
                    onChange={(e) => setPhone(e.target.value)}
                    required
                  />
                </div>
                <div>
                  <label className="block text-gray-700 mb-2">CPF/CNPJ *</label>
                  <input 
                    type="text" 
                    className="w-full p-2 border rounded focus:ring-2 focus:ring-blue-500" 
                    placeholder="CPF ou CNPJ" 
                    value={cpfCnpj}
                    onChange={(e) => setCpfCnpj(e.target.value)}
                    required
                  />
                </div>
              </div>

              {!editMode && (
                <div>
                  <label className="block text-gray-700 mb-2">Senha *</label>
                  <input 
                    type="password" 
                    className="w-full p-2 border rounded focus:ring-2 focus:ring-blue-500" 
                    placeholder="Senha" 
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required={!editMode}
                  />
                </div>
              )}

              <div className="mt-6 flex gap-4">
                <button 
                  type="submit" 
                  className="px-6 py-2 bg-green-500 text-white rounded hover:bg-green-600 transition-colors duration-200"
                >
                  {editMode ? 'Atualizar' : 'Salvar'}
                </button>
                {editMode && (
                  <button 
                    type="button" 
                    className="px-6 py-2 bg-red-500 text-white rounded hover:bg-red-600 transition-colors duration-200"
                    onClick={handleCancelEdit}
                  >
                    Cancelar
                  </button>
                )}
              </div>
            </form>
          </div>
        </div>

        {/* Users List */}
        <div>
          <div className="flex justify-between items-center mb-4">
            <h2 className="text-2xl font-semibold">Lista de Usuários</h2>
            <input
              type="text"
              placeholder="Pesquisar usuários..."
              className="p-2 border rounded"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
            />
          </div>
          
          <div className="bg-white shadow-md rounded-lg overflow-hidden">
            <table className="w-full border-collapse">
              <thead>
                <tr className="bg-gray-200 text-left">
                  <th className="p-3">Nome</th>
                  <th className="p-3">Email</th>
                  <th className="p-3">Telefone</th>
                  <th className="p-3">CPF/CNPJ</th>
                  <th className="p-3">Ações</th>
                </tr>
              </thead>
              <tbody>
                {filteredUsers.length > 0 ? (
                  filteredUsers.map((user) => (
                    <tr key={user.id} className="border-b hover:bg-gray-100">
                      <td className="p-3">{user.nome}</td>
                      <td className="p-3">{user.email}</td>
                      <td className="p-3">{user.telefone}</td>
                      <td className="p-3">{user.cpfCnpj}</td>
                      <td className="p-3">
                        <div className="flex space-x-2">
                          <button 
                            onClick={() => handleSelectUser(user.id)}
                            className="text-blue-500 hover:text-blue-700"
                          >
                            Editar
                          </button>
                          <button 
                            onClick={() => handleDeleteUser(user.id)}
                            className="text-red-500 hover:text-red-700"
                          >
                            Excluir
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan={5} className="p-4 text-center text-gray-500">
                      {users.length === 0 ? "Nenhum usuário encontrado. Adicione um novo usuário." : "Nenhum usuário corresponde à pesquisa."}
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </div>
      </main>
    </div>
  );
};

export default UserManagement;