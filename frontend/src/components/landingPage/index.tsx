// SuportFlow/frontend/src/components/landingPage/index.tsx
import logo from "../../assets/logo.png";
import { Link } from "@tanstack/react-router";
import {
  FaTicketAlt, // Icon for Ticket System
  FaComments,   // Icon for Integrated Chat
  FaChartBar,   // Icon for Analytics/Control
} from "react-icons/fa"; // Import react-icons
import baner from "../../assets/imagemsupportflow.png"

function Index() {
  return (
    <div className="min-h-screen flex flex-col font-sans">
      {/* Header */}
      <header className="bg-gray-800 py-4 shadow-md sticky top-0 z-50">
        <div className="container mx-auto flex items-center justify-between px-4">
          <div className="flex items-center">
            <img
              src={logo}
              alt="SupportFlowAI Logo"
              className="h-12 w-auto mr-2"
            />
            <span className="text-lg font-bold text-white">SupportFlow</span>
          </div>
          {/* Container para os links e botão (Alinhados à direita) */}
          <div className="flex items-center">
            <nav>
              <ul className="flex space-x-4 md:space-x-6">
                {" "}
                {/* Ajuste space-x para telas menores */}
                <li>
                  <a
                    href="#sobre"
                    className="text-white hover:text-green-600 transition"
                  >
                    Sobre
                  </a>
                </li>
                <li>
                  <a
                    href="#recursos"
                    className="text-white hover:text-green-600 transition"
                  >
                    Recursos
                  </a>
                </li>
                <li>
                  <a
                    href="#contato"
                    className="text-white hover:text-green-600 transition"
                  >
                    Contato
                  </a>
                </li>
              </ul>
            </nav>
            <Link
              to="/login"
              className="bg-green-600 hover:bg-green-500 text-white font-bold py-2 px-4 rounded transition ml-4"
            >
              {/* ml-4 adicionado */}
              Login / Singup
            </Link>
          </div>
        </div>
      </header>

            {/* Main Content */}
            <main className="flex-grow">
        {/* Banner */}
        <section className="bg-green-700 text-white py-16 md:py-20 text-center">
          {/* Ajustado py */}
          <div className="container mx-auto px-4">
            <h1 className="text-3xl md:text-4xl font-bold mb-4">
              SupportFlow
            </h1>{" "}
            {/* Ajustado text-xl */}
            <p className="text-base md:text-xl mb-8">
              A Solução Completa para Suporte ao Cliente<br/>
            </p>{""} {/* Adicionado mb-8 para espaçamento */}
            {/* Image */}
            <img
              src={baner} // Replace with the actual path to your image
              alt="SupportFlow Banner Image"
              className="mx-auto w-full md:w-3/4 lg:w-1/2" // Example responsive widths. Adjust as needed.
            />
          </div>
        </section>

        {/* About */}
        <section id="sobre" className="py-12 md:py-16">
          {/* Ajustado py */}
          <div className="container mx-auto px-4">
            <h2 className="text-2xl md:text-3xl font-bold text-center mb-6 md:mb-8">
              Sobre
            </h2>{" "}
            {/* Ajustado text-2xl e mb */}
            <p className="text-gray-700 text-base md:text-lg text-center">
              {/* Ajustado text-base */}
              O SupportFlow é uma solução SaaS (Software as a Service) abrangente desenvolvida para otimizar o suporte ao cliente em empresas de todos os portes.
              Esta plataforma integra múltiplas ferramentas essenciais para o gerenciamento eficiente do atendimento ao cliente.
              O SupportFlow se destaca pela sua interface intuitiva e pela capacidade de se adaptar ao fluxo de trabalho específico de cada empresa, tornando-se uma ferramenta indispensável para equipes de suporte ao cliente que buscam excelência no atendimento.
            </p>
          </div>
        </section>

        {/* Features */}
        <section id="recursos" className="py-12 md:py-16 bg-black">
          {/* Ajustado py */}
          <div className="container mx-auto px-4 bg-gray800">
            <h2 className="text-gray-400 text-2xl md:text-3xl font-bold text-center mb-8 md:mb-12">
              Recursos
            </h2>{" "}
            {/* Ajustado text-2xl e mb */}
            <div className="grid grid-cols-1 md:grid-cols-3 gap-6 md:gap-8">
              {/* Feature Card 1 */}
              <div className="bg-gray-400 rounded-lg shadow-md p-6 hover:shadow-lg transition flex flex-col justify-center items-center">
                <h3 className="text-black text-lg md:text-xl font-semibold mb-2 md:mb-4 text-center flex items-center">
                  <FaTicketAlt className="mr-2" /> {/* Added Icon */}
                Sistema de Chamados
                </h3>{" "}
                {/* Ajustado text-lg e mb */}
                <p className="text-black text-sm md:text-base text-center">
                  {" "}
                  {/* Ajustado text-sm */}
                  Gerenciamento organizado de solicitações de suporte com priorização automática, atribuição de responsáveis e acompanhamento de prazos.
                </p>
              </div>
              {/* Feature Card 2 */}
              <div className="bg-gray-400 rounded-lg shadow-md p-6 hover:shadow-lg transition flex flex-col justify-center items-center">
                <h3 className="text-black text-lg md:text-xl font-semibold mb-2 md:mb-4 text-center flex items-center">
                  <FaComments className="mr-2" /> {/* Added Icon */}
                Chat Integrado
                </h3>{" "}
                {/* Ajustado text-lg e mb */}
                <p className="text-black text-sm md:text-base text-center">
                  {" "}
                  {/* Ajustado text-sm */}
                  Comunicação em tempo real entre atendentes e clientes, permitindo resoluções rápidas e eficientes.
                </p>
              </div>
              {/* Feature Card 3 */}
              <div className="bg-gray-400 rounded-lg shadow-md p-6 hover:shadow-lg transition flex flex-col justify-center items-center">
                <h3 className="text-black text-lg md:text-xl font-semibold mb-2 md:mb-4 text-center flex items-center">
                  <FaChartBar className="mr-2" /> {/* Added Icon */}
                Controle de Atendimento
                </h3>{" "}
                {/* Ajustado text-lg e mb */}
                <p className="text-black text-sm md:text-base text-center">
                  {" "}
                  {/* Ajustado text-sm */}
                  Métricas detalhadas sobre tempo de resposta, taxa de resolução e satisfação do cliente.
                </p>
              </div>
            </div>
          </div>
        </section>

        {/* Contact */}
        <section id="contato" className="py-12 md:py-16 bg-gray-300">
          {" "}
          {/* Ajustado py */}
          <div className="container mx-auto px-4">
            <h2 className="text-2xl md:text-3xl font-bold text-center mb-6 md:mb-8">
              Entre em Contato
            </h2>{" "}
            {/* Ajustado text-2xl e mb */}
            <div className="text-center">
              <p className="mb-2 md:mb-4">
                {" "}
                {/* Ajustado mb */}
                <span className="font-semibold">Telefone:</span>
                <a
                  href="tel:+1234567890"
                  className="text-gray-700 hover:text-green-600"
                >
                  {" "}
                  +123 456 7890
                </a>
              </p>
              <p>
                <span className="font-semibold">Email:</span>
                <a
                  href="mailto:contato@suportflowai.com"
                  className="text-gray-700 hover:text-green-600"
                >
                  {" "}
                  contato@suportflow.com
                </a>
              </p>
            </div>
          </div>
        </section>
      </main>

      {/* Footer */}
      <footer className="bg-gray-800 text-white py-6">
        <div className="container mx-auto px-4 text-center">
          <div className="flex flex-col md:flex-row justify-center space-y-2 md:space-y-0 md:space-x-4 mb-4">
            {/* Ajustes para mobile e desktop */}
            <a
              href="#termos"
              className="text-white hover:text-green-600 transition"
            >
              Termos de Uso
            </a>
            <a
              href="#privacidade"
              className="text-white hover:text-green-600 transition"
            >
              Política de Privacidade
            </a>
            <a
              href="#faq"
              className="text-white hover:text-green-600 transition"
            >
              FAQ
            </a>
          </div>
          <p className="text-sm">
            © 2025 SupportFlow. Todos os direitos reservados.
          </p>
        </div>
      </footer>
    </div>
  );
}

export default Index;
