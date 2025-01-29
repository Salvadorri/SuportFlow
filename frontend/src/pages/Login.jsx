import "../styles/App.css";
import Footer from "../components/layout/Footer.jsx";
import HeaderHome from "../components/specific/users/HeaderHome.jsx";
import Banner from "../components/specific/users/banner.jsx";
import About from "../components/specific/users/About.jsx";
import Features from "../components/specific/users/Features.jsx";
import Contact from '../components/specific/users/Contact.jsx'


function LoginForm() {
  const [count, setCount] = useState(0); // Você não está usando count no momento, pode remover se não for necessário depois

  return (
    <div>
      <HeaderHome/>
      <Banner/>
      <About/>
      <Features/>
      <Contact/>
      <Footer/>
    </div>
  );
}

export default LoginForm;
