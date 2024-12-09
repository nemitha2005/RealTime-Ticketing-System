import React, { useState, useEffect } from "react";
import "./ControlPanel.css";
import { motion } from "framer-motion";
import { GoDotFill } from "react-icons/go"; // Import GoDotFill
import axios from "axios";

const container = (delay) => ({
  hidden: { x: 100, opacity: 0 },
  visible: {
    x: 0,
    opacity: 1,
    transition: { duration: 0.6, delay: delay },
  },
});

const NotificationBanner = ({ message, type, onClose }) => {
  return (
    <div className={`notification-banner ${type}`}>
      <div className="notification-content">
        <p>{message}</p>
      </div>
    </div>
  );
};

const ControlPanel = () => {
  const [notification, setNotification] = useState(null);
  const [status, setStatus] = useState({ text: "Loading...", color: "gray" });

  useEffect(() => {
    const fetchStatus = async () => {
      try {
        const response = await axios.get("http://localhost:8080/api/status");
        if (response.data.includes("running")) {
          setStatus({ text: "Running", color: "green" });
        } else {
          setStatus({ text: "Stopped", color: "red" });
        }
      } catch (error) {
        console.error("Error fetching status:", error);
        setStatus({ text: "Error", color: "gray" });
      }
    };

    fetchStatus();
    const interval = setInterval(fetchStatus, 5000);
    return () => clearInterval(interval);
  }, []);

  const showNotification = (message, type) => {
    setNotification({ message, type });
    setTimeout(() => setNotification(null), 3000);
  };

  const handleStart = async () => {
    try {
      const response = await axios.post("http://localhost:8080/api/start");
      showNotification(response.data, "success");
    } catch (error) {
      console.error("Error starting the system:", error);
      showNotification(
        error.response?.data || "Failed to start the system.",
        "error"
      );
    }
  };

  const handleStop = async () => {
    try {
      const response = await axios.post("http://localhost:8080/api/stop");
      showNotification(response.data, "success");
    } catch (error) {
      console.error("Error stopping the system:", error);
      showNotification(
        error.response?.data || "Failed to stop the system.",
        "error"
      );
    }
  };

  return (
    <motion.div
      variants={container(0.3)}
      initial="hidden"
      animate="visible"
      className="control-panel"
    >
      <div className="status">
      <GoDotFill style={{ color: status.color }} size={16} />
      <span className="status-label" style={{ color: status.color }}>
        {status.text}
      </span>
      </div>
      <div className="button-group">
        <button className="control-button start-button" onClick={handleStart}>
          Start System
        </button>
        <button className="control-button stop-button" onClick={handleStop}>
          Stop System
        </button>
      </div>
      {notification && (
        <NotificationBanner
          message={notification.message}
          type={notification.type}
          onClose={() => setNotification(null)}
        />
      )}
    </motion.div>
  );
};

export default ControlPanel;
