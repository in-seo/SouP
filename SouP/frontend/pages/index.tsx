import {
  Box,
  Flex,
  SectionBody,
  SectionBodyAlt,
  ProfilePlaceholder,
  Hr,
  Label,
  CarouselPagination,
} from "common/components";
import { GetServerSideProps, NextPage } from "next";
import { useRouter } from "next/router";
import Image from "next/image";
import { css } from "@emotion/react";
import { dehydrate, QueryClient, useQuery } from "react-query";
import { http } from "common/services";
import { ellipsis } from "polished";
import { ChildrenContainer } from "components";
import {
  breakpoints,
  ISource,
  SourceDictionary,
  SourceList,
  timeDiffString,
  toMatrix,
} from "utils";
import {
  Dispatch,
  Fragment,
  SetStateAction,
  useMemo,
  useRef,
  useState,
} from "react";
import { getDisplayTag, ITag } from "utils/tagDictionary";
import { Swiper, SwiperSlide } from "swiper/react";
import SwiperType, { Autoplay } from "swiper";
import React from "react";
import Link from "next/link";
import { IPostPreviewContent, ILoungePost } from "types";

/* -------------------------------------------------------------------------- */
/*                                    utils                                   */
/* -------------------------------------------------------------------------- */

const useSource = (initialSource: ISource) => {
  const [source, setSource] = useState<ISource>(initialSource);
  return [source, setSource] as [ISource, Dispatch<SetStateAction<ISource>>];
};

/* -------------------------------------------------------------------------- */
/*                                   styles                                   */
/* -------------------------------------------------------------------------- */

const FeaturedSectionStyle = css({
  paddingTop: "32px",
  [breakpoints.at("sm")]: {
    paddingTop: "24px",
  },
  paddingBottom: "44px",
  border: "0px",
  borderBottom: "1px solid var(--outline)",
  marginBottom: "36px",
});

/* -------------------------------------------------------------------------- */
/*                                 components                                 */
/* -------------------------------------------------------------------------- */

const HotItem = ({ post }: { post: IPostPreviewContent }) => {
  const styles = {
    Wrapper: css`
      gap: 16px;
      flex: 0 1 320px;
      overflow: hidden;
      cursor: pointer;
    `,
    PostName: css`
      font-size: 16px;
      font-weight: 600;
      ${ellipsis(undefined, 2)}
    `,
    PostContent: css`
      font-size: 13px;
      ${ellipsis(undefined, 2)}
    `,
  };

  return (
    <Link passHref href={`/projects/${post.id}`}>
      <Flex as="a" column css={styles.Wrapper}>
        <Image
          alt="hot1"
          src="https://i.imgur.com/hVe2ScX.png"
          width={320}
          height={180}
        />
        <Flex column css={{ gap: "8px" }}>
          <p css={styles.PostName}>{post.postName}</p>
          <p css={styles.PostContent}>{post.content}</p>
        </Flex>
      </Flex>
    </Link>
  );
};

const NewItem = ({ post }: { post: IPostPreviewContent }) => {
  const router = useRouter();

  return (
    <Link passHref href={`/projects/${post.id}`}>
      <Flex as="a" css={{ gap: "16px", cursor: "pointer" }}>
        <span css={{ flex: "0 0 auto", height: "75px" }}>
          <Image
            alt="hot1"
            src="https://i.imgur.com/oEdONmz.jpeg"
            width={100}
            height={75}
          />
        </span>
        <Flex
          column
          css={{ flex: "1", width: 0, gap: "4px", overflow: "hidden" }}
        >
          <p
            css={{
              fontSize: "16px",
              fontWeight: "600",
              ...ellipsis(),
            }}
          >
            {post.postName}
          </p>
          <p
            css={{
              fontSize: "13px",
              ...ellipsis(undefined, 2),
            }}
          >
            {post.content}
          </p>
        </Flex>
      </Flex>
    </Link>
  );
};

const PostItem = ({ post }: { post: IPostPreviewContent }) => {
  const router = useRouter();

  return (
    <Link passHref href={`/projects/${post.id}`}>
      <Flex
        as="a"
        css={{ gap: "16px", alignItems: "stretch", cursor: "pointer" }}
      >
        <Flex
          column
          css={{
            gap: "16px",
            alignItems: "center",
            flex: "0 0 auto",
          }}
        >
          <ProfilePlaceholder size={32} value={post.userName} />
        </Flex>
        <Flex
          column
          css={{ gap: "4px", flex: "1 1 auto", overflow: "hidden", width: 0 }}
        >
          <p
            css={{
              fontSize: "16px",
              fontWeight: "600",
              ...ellipsis(),
            }}
          >
            {post.postName}
          </p>
          <p
            css={{
              fontSize: "14px",
              whiteSpace: "pre-line",
              ...ellipsis(undefined, 3),
            }}
          >
            {post.content}
          </p>
          <Flex css={{ gap: "12px", marginTop: "4px" }}>
            {post.stacks.map((stack) => (
              <Label size="smaller" key={stack}>
                {getDisplayTag(stack)}
              </Label>
            ))}
          </Flex>
        </Flex>
      </Flex>
    </Link>
  );
};

const LoungeItem = ({ post }: { post: ILoungePost }) => {
  return (
    <Flex css={{ gap: "12px", alignItems: "stretch" }}>
      <Flex
        column
        css={{
          gap: "16px",
          alignItems: "center",
          flex: "0 0 auto",
        }}
      >
        <Image
          css={{
            width: "32px",
            height: "32px",
            borderRadius: "99999px",
            overflow: "hidden",
          }}
          width="32px"
          height="32px"
          alt="profile"
          src={post.picture}
        />
      </Flex>
      <Flex column css={{ gap: "4px", flex: "1 1 auto" }}>
        <Flex css={{ justifyContent: "space-between", alignItems: "center" }}>
          <p css={{ fontSize: "14px" }}>
            <b>{post.username}</b> · {timeDiffString(post.date)}
          </p>
        </Flex>
        <div
          css={{
            fontSize: "14px",
            whiteSpace: "pre-line",
            ...ellipsis(undefined, 3),
          }}
        >
          {post.content}
        </div>
      </Flex>
    </Flex>
  );
};

const Projects = () => {
  const styles = {
    Wrapper: css({
      flex: "99999 1 480px",
      gap: "12px",
    }),
    HeaderWrapper: css({
      fontSize: "20px",
      fontWeight: "700",
      "& > *+*": { marginLeft: "12px" },
    }),
  };

  const { data, isLoading, isError } = useQuery(
    "front/projects",
    fetchFrontProjects
  );
  const [source, setSource] = useSource(
    SourceList[(Math.random() * SourceList.length) | 0]
  );

  if (!data || isLoading || isError)
    return <Flex column css={{ flex: "3 1 480px" }} />;
  console.log(data);
  return (
    <Flex column css={styles.Wrapper}>
      <p css={styles.HeaderWrapper}>
        {SourceList.map((currentSource, i) => (
          <span
            onClick={() => setSource(currentSource)}
            key={i}
            css={{
              color: source === currentSource ? undefined : "var(--disabled)",
              fontWeight: source === currentSource ? undefined : 500,
              cursor: "pointer",
            }}
          >
            {SourceDictionary[currentSource]}
          </span>
        ))}
      </p>
      <Box responsive column css={{ gap: "16px", padding: "16px 12px" }}>
        {data[source].map((post, i) => (
          <Fragment key={post.id}>
            {i !== 0 && <Hr />}
            <PostItem post={post} />
          </Fragment>
        ))}
      </Box>
    </Flex>
  );
};

const Lounge = () => {
  const styles = {
    Wrapper: css({ flex: "1 1 440px", gap: "12px" }),
    HeaderWrapper: css({
      fontSize: "20px",
      fontWeight: "700",
    }),
    ContentWrapper: css({
      gap: "16px",
      padding: "16px 12px",
      cursor: "pointer",
    }),
  };

  const { data, isLoading, isError } = useQuery("lounge", fetchLounge);

  if (!data || isLoading || isError) return null;

  return (
    <Flex column css={styles.Wrapper}>
      <p css={styles.HeaderWrapper}>
        <span>라운지</span>
      </p>
      <Link passHref href="/lounge">
        <Box as="a" responsive column css={styles.ContentWrapper}>
          {data.map((post, i) => (
            <Fragment key={post.lounge_id}>
              {i !== 0 && <Hr />}
              <LoungeItem post={post} />
            </Fragment>
          ))}
        </Box>
      </Link>
    </Flex>
  );
};

const HotFeatured = () => {
  const { data, isLoading, isError } = useQuery(
    "front/featured",
    fetchFrontFeatured
  );

  const content = useMemo(() => toMatrix(data?.HOT || [], 2), [data]);
  const [pagination, setPagination] = useState(0);

  const swiperRef = useRef<SwiperType | null>(null);

  if (!data || isLoading || isError) return null;

  return (
    <Flex
      column
      css={{
        flex: "99999 1 480px",
        gap: "24px",
        minWidth: 0,
        marginBottom: "-36px",
        img: {
          borderRadius: "8px",
        },
      }}
    >
      <Flex css={{ alignItems: "center", justifyContent: "space-between" }}>
        <p css={{ fontSize: "20px", fontWeight: "bold" }}>
          Hot 스터디/프로젝트 🔥
        </p>
        <CarouselPagination
          swiperRef={swiperRef}
          current={pagination}
          end={content.length}
        />
      </Flex>
      <Swiper
        onSwiper={(ref) => (swiperRef.current = ref)}
        loop={true}
        autoplay={{
          delay: 5000,
          disableOnInteraction: false,
        }}
        modules={[Autoplay]}
        spaceBetween={72}
        onSlideChange={(swiper) => setPagination(swiper.realIndex)}
      >
        {content.map((content, i) => (
          <SwiperSlide key={i}>
            <Flex
              css={{
                gap: "36px",
                [breakpoints.at("sm")]: {
                  gap: "24px",
                },
              }}
            >
              {content.map((post) => (
                <HotItem key={post.id} post={post} />
              ))}
            </Flex>
          </SwiperSlide>
        ))}
      </Swiper>
    </Flex>
  );
};

const NewFeatured = () => {
  const { data, isLoading, isError } = useQuery(
    "front/featured",
    fetchFrontFeatured
  );

  if (!data || isLoading || isError) return null;

  return (
    <Flex
      column
      css={{
        flex: "1 1 440px",
        gap: "24px",
        img: {
          borderRadius: "6px",
        },
        maxWidth: "676px",
      }}
    >
      <p
        css={{
          fontSize: "20px",
          fontWeight: "bold",
        }}
      >
        New 스터디/프로젝트 ✨
      </p>
      <Flex column css={{ gap: "24px" }}>
        {data.NEW.slice(0, 3).map((post) => (
          <NewItem key={post.id} post={post} />
        ))}
      </Flex>
    </Flex>
  );
};

const Featured = () => {
  return (
    <Flex
      css={{
        flexWrap: "wrap",
        gap: "96px",
        overflow: "hidden",
      }}
    >
      <HotFeatured />
      <NewFeatured />
    </Flex>
  );
};

const Banner = React.memo(() => {
  const styles = {
    Wrapper: css`
      height: 300px;
      --banner-title: 24px;
      --banner-description: 16px;
      --banner-image-size: 240px;
      padding-top: 48px;
      padding-bottom: 48px;
      ${breakpoints.at("sm")} {
        height: 200px;
        --banner-title: 16px;
        --banner-description: 14px;
        --banner-image-size: 128px;
        padding: 24px 18px !important;
      }
      background-color: #111;
      color: #ffffff;
      border-bottom: 1px solid var(--outline);
      margin-bottom: 0;
      z-index: -2;
    `,
    InnerWrapper: css({
      height: "100%",
      alignItems: "center",
      justifyContent: "space-between",
      position: "relative",
    }),
    BannerContentWrapper: css`
      height: 100%;
      justify-content: space-between;
      align-items: flex-start;
    `,
    BannerBadge: css`
      color: #111;
      font-weight: 700;
      font-size: var(--banner-description);
      padding: 0.5em 0.65em;
    `,
    BannerTitle: css`
      font-weight: 700;
      font-size: var(--banner-title);
    `,
    BannerDescription: css`
      font-size: var(--banner-description);
    `,
    BannerImage: css`
      position: absolute;
      right: 0;
      z-index: 1;
      width: var(--banner-image-size);
    `,
  };

  const BannerLabel = () => (
    <Label variant="primary" size="freeform" css={styles.BannerBadge}>
      프로모션
    </Label>
  );
  const BannerTitle = () => (
    <>
      프론트엔드 BEST 강의
      <br />
      SouP에서만 30% 할인중👌
    </>
  );
  const BannerDescription = () => <>입문부터 실전까지, 믿고 보는 실무자 Pick</>;
  const BannerImage = () => (
    <Image
      alt="front-banner"
      src="https://i.imgur.com/7FfQL9b.png"
      width="240"
      height="240"
    />
  );

  return (
    <SectionBodyAlt css={styles.Wrapper}>
      <Flex css={styles.InnerWrapper}>
        <Flex column css={styles.BannerContentWrapper}>
          <BannerLabel />
          <Flex column css={{ gap: "12px" }}>
            <p css={styles.BannerTitle}>
              <BannerTitle />
            </p>
            <p css={styles.BannerDescription}>
              <BannerDescription />
            </p>
          </Flex>
        </Flex>
        <span css={styles.BannerImage}>
          <BannerImage />
        </span>
      </Flex>
    </SectionBodyAlt>
  );
});
Banner.displayName = "Banner";

/* -------------------------------------------------------------------------- */
/*                                    page                                    */
/* -------------------------------------------------------------------------- */

const Home: NextPage = () => {
  return (
    <ChildrenContainer>
      <Banner />
      <SectionBodyAlt css={FeaturedSectionStyle}>
        <Featured />
      </SectionBodyAlt>
      <SectionBody>
        <Flex css={{ gap: "36px", flexWrap: "wrap" }}>
          <Projects />
          <Lounge />
        </Flex>
      </SectionBody>
    </ChildrenContainer>
  );
};

/* -------------------------------------------------------------------------- */
/*                                     api                                    */
/* -------------------------------------------------------------------------- */

const fetchFrontProjects = async () => {
  const res = await http.get<{
    SOUP: IPostPreviewContent[];
    OKKY: IPostPreviewContent[];
    INFLEARN: IPostPreviewContent[];
    CAMPICK: IPostPreviewContent[];
    HOLA: IPostPreviewContent[];
  }>("/front/projects");
  return res.data;
};

const fetchFrontFeatured = async () => {
  const res = await http.get<{
    NEW: IPostPreviewContent[];
    HOT: IPostPreviewContent[];
  }>("/front/featured");
  return res.data;
};

const fetchLounge = async () => {
  const res = await http.get<ILoungePost[]>("/lounge");
  return res.data;
};

export const getServerSideProps: GetServerSideProps = async () => {
  const queryClient = new QueryClient();

  let res = await Promise.all([
    queryClient.prefetchQuery("front/featured", fetchFrontFeatured),
    // queryClient.prefetchQuery("front/projects", fetchFrontProjects),
  ]);

  console.log(res);

  return {
    props: {
      dehydratedState: dehydrate(queryClient),
    },
  };
};

export default Home;
