import "../styles/tailwind.css";

function ForgotPassword() {
  return (
    <div className="flex items-center justify-center min-h-screen bg-gray-900">
      <div className="w-full max-w-md p-8 space-y-6 bg-gray-800 rounded-xl shadow-2xl">
        <h2 className="text-3xl font-bold text-center text-green-500">SuportFlowAI</h2>

        <h3 className="text-lg font-medium text-center text-gray-300"> {/* Alterado para h3 e text-lg */}
          Recuperação de Senha
        </h3>
        <div>
          <label htmlFor="email" className="block mb-2 text-sm font-medium text-gray-300">
            E-mail
          </label>
          <input
            type="email"
            id="email"
            placeholder="seu@email.com"
            className="bg-gray-700 border border-gray-600 text-white text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
            required
          />
        </div>

        <button
          type="button"  // Importante: type="button"
          className="w-full text-white bg-green-600! hover:bg-green-800! focus:ring-4 focus:outline-none focus:ring-blue-200 font-medium rounded-lg text-sm px-5 py-2.5 text-center"
        >
          Enviar E-mail
        </button>
      </div>
    </div>
  );
}

export default ForgotPassword;