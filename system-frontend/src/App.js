import "./App.css";
import ConfigurationForm from "./components/ConfigurationForm";
import ControlPanel from "./components/ControlPanel";
import Header from "./components/Header";
import LogDisplay from "./components/LogDisplay";


function App() {
  return (
    <div className="App">
      <Header />
      <ConfigurationForm />
      <ControlPanel />
        <LogDisplay />
    </div>
  );
}

export default App;
