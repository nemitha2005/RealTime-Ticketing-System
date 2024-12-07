import React from "react";
import "./ControlPanel.css";
import { motion } from "framer-motion";

const container = (delay) => ({
  hidden: { x: -100, opacity: 0 },
  visible: {
    x: 0,
    opacity: 1,
    transition: { duration: 0.6, delay: delay },
  },
});

const ControlPanel = () => {
  return (
    <motion.div
      variants={container(0.3)}
      initial="hidden"
      animate="visible"
      className="control-panel"
    >
      <h2>Control Panel</h2>
      <div className="button-group">
        <button className="control-button start-button">Start System</button>
        <button className="control-button stop-button">Stop System</button>
      </div>
    </motion.div>
  );
};

export default ControlPanel;
