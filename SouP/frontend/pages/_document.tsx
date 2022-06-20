import { Html, Head, Main, NextScript } from "next/document";

export default function Document() {
  const setInitialAuth = `function getAuthCache() {
  if (window.sessionStorage.getItem("auth")) {
    try {
      return JSON.parse(window.sessionStorage.getItem("auth"));
    } catch (e) {
      return { success: false };
    }
  }
  return { success: false };
};
document.body.dataset.auth = getAuthCache();`;
  return (
    <Html lang="ko">
      <Head>
        <link
          rel="stylesheet preload prefetch"
          as="style"
          type="text/css"
          crossOrigin="anonymous"
          href="https://cdn.jsdelivr.net/gh/orioncactus/pretendard/dist/web/static/pretendard.css"
        />
      </Head>
      <body>
        <script dangerouslySetInnerHTML={{ __html: setInitialAuth }} />
        <div id="portal" />
        <Main />
        <NextScript />
      </body>
    </Html>
  );
}
