import ReactDOM from "react-dom";

const Portal = ({ children, at }: { children: React.ReactNode; at: any }) => {
  const element = typeof window !== "undefined" && document.querySelector(at);
  return element && children ? ReactDOM.createPortal(children, element) : null;
};

export default Portal;
