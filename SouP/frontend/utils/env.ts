let isDevEnv = false;

if (process && process.env.NODE_ENV === "development") {
  isDevEnv = true;
}

export { isDevEnv };
