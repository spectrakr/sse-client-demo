import { defineConfig, loadEnv } from "vite";

export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), "");
  return {
    server: {
      open: false,
      host: "0.0.0.0",
      port: 5173,
    },
  };
});
