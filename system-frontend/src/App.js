import "./App.css";
import ConfigurationForm from "./components/ConfigurationForm";
import ControlPanel from "./components/ControlPanel";
import Header from "./components/Header";

function App() {
  return (
    <div className="App">
      <Header />
      <ConfigurationForm />
      <ControlPanel />
    </div>
  );
}

export default App;
