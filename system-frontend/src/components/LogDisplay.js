import React, { useEffect, useState, useRef } from "react";
import "./LogDisplay.css";
import { motion } from "framer-motion";

const container = (delay) => ({
  hidden: { x: -100, opacity: 0 },
  visible: {
    x: 0,
    opacity: 1,
    transition: { duration: 0.6, delay: delay },
  },
});

const LogDisplay = () => {
  const [logs, setLogs] = useState([]);
  const logContainerRef = useRef(null);

  useEffect(() => {
    const eventSource = new EventSource(
      "http://localhost:8080/api/logs/stream"
    );
    eventSource.onmessage = (event) =>
      setLogs((prevLogs) => [...prevLogs, event.data]);
    eventSource.onerror = () => eventSource.close();
    return () => eventSource.close();
  }, []);

  useEffect(() => {
    if (logContainerRef.current) {
      logContainerRef.current.scrollTop = logContainerRef.current.scrollHeight;
    }
  }, [logs]);
  return (
    <motion.div
      variants={container(0.3)}
      initial="hidden"
      animate="visible"
      className="log-display"
    >
      <h2>System Logs</h2>
      <div className="log-container" ref={logContainerRef}>
        <ul className="log-list">
          {logs.map((log, index) => (
            <li key={index}>{log}</li>
          ))}
        </ul>
      </div>
    </motion.div>
  );
};

export default LogDisplay;
