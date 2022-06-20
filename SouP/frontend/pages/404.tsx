import { NextPage } from "next";
import { Error } from "components";

const Error404: NextPage = () => {
  return <Error status={404} />;
};

export default Error404;
