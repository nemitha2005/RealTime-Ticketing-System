import React from "react";
import "./ConfigurationForm.css";
import { motion } from "framer-motion";

const container = (delay) => ({
  hidden: { x: -100, opacity: 0 },
  visible: {
    x: 0,
    opacity: 1,
    transition: { duration: 0.5, delay: delay },
  },
});

const ConfigurationForm = () => {
  return (
    <motion.div
      variants={container(0.3)}
      initial="hidden"
      animate="visible"
      className="form-container"
    >
      <h2>Create Configuration</h2>
      <form>
        <div className="form-group">
          <label htmlFor="totalTickets">Total Tickets</label>
          <input type="number" id="totalTickets" placeholder="Enter a value" />
        </div>

        <div className="form-group">
          <label htmlFor="ticketReleaseRate">Ticket Release Rate</label>
          <input
            type="number"
            id="ticketReleaseRate"
            placeholder="Enter a value"
          />
        </div>

        <div className="form-group">
          <label htmlFor="customerRetrievalRate">Customer Retrieval Rate</label>
          <input
            type="number"
            id="customerRetrievalRate"
            placeholder="Enter a value"
          />
        </div>

        <div className="form-group">
          <label htmlFor="maxTicketCapacity">Max Ticket Capacity</label>
          <input
            type="number"
            id="maxTicketCapacity"
            placeholder="Enter a value"
          />
        </div>

        <div className="form-buttons">
          <button type="button" className="update-button">
            Update Configuration
          </button>
          <button type="button" className="get-button">
            Check Configuration
          </button>
        </div>
      </form>
    </motion.div>
  );
};

export default ConfigurationForm;
