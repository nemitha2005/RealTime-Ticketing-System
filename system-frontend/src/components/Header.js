import React, { useContext, useState} from "react";
import { IoTicket } from "react-icons/io5";
import { MdOutlineLightMode } from "react-icons/md";
import { ThemeContext } from "../ThemeContext";
import "./Header.css";

const Header = () => {
  const { theme, setTheme } = useContext(ThemeContext);
  const [dropdownOpen, setDropdownOpen] = useState(false);
  

  const toggleDropdown = () => {
    setDropdownOpen(!dropdownOpen);
  };

  const handleThemeChange = (newTheme) => {
    setTheme(newTheme);
    setDropdownOpen(false);
  };

  return (
    <header className={`header ${theme}`}>
      <div className="header-container">
        <div className="header-left">
          <IoTicket size={35} className="header-icon" />
          <p className="header-title">Ticket Simulation Control Panel</p>
        </div>
  
        <div className="header-right">
          <button className="theme-toggle-button" onClick={toggleDropdown}>
            <MdOutlineLightMode size={35} />
          </button>
          {dropdownOpen && (
            <ul className="theme-dropdown">
              <li onClick={() => handleThemeChange("light")}>Light Mode</li>
              <li onClick={() => handleThemeChange("dark")}>Dark Mode</li>
              <li onClick={() => handleThemeChange("system")}>
                System Default
              </li>
            </ul>
          )}
        </div>
      </div>
    </header>
  );
};

export default Header;
