/** @type {import('next').NextConfig} */
const nextConfig = {
  reactStrictMode: true,
  images: {
    domains: ["i.imgur.com", "lh3.googleusercontent.com"],
  },
  experimental: {
    scrollRestoration: "manual",
  },
  async rewrites() {
    return [
      {
        source: "/api/:path*",
        destination: `http://localhost:8080/:path*`,
      },
    ];
  },
};

module.exports = nextConfig;
