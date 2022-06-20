import { css } from "@emotion/react";
import {
  Flex,
  Box,
  Button,
  SectionHeader,
  SectionBody,
  Hr,
} from "common/components";
import { http } from "common/services";
import { ChildrenContainer } from "components";
import useAuth from "hooks/useAuth";
import { GetServerSideProps } from "next";
import Image from "next/image";
import { Fragment, useEffect } from "react";
import { useForm } from "react-hook-form";
import { MdOutlineFavorite, MdOutlineFavoriteBorder } from "react-icons/md";
import { dehydrate, QueryClient, useQuery, useQueryClient } from "react-query";
import ReactTextareaAutosize from "react-textarea-autosize";
import { ILoungePost } from "types";
import { injectSession, timeDiffString } from "utils";

/* -------------------------------------------------------------------------- */
/*                                 components                                 */
/* -------------------------------------------------------------------------- */

const LoungePost = ({ post }: { post: ILoungePost }) => {
  const queryClient = useQueryClient();
  const auth = useAuth();

  const handleFav = async ({ user_id, isfav, lounge_id }: ILoungePost) => {
    if (!auth.success || user_id === auth.user_id) return;
    const res = await http.post("/lounge/fav", {
      id: lounge_id,
      mode: !isfav,
    });
    const data = res.data;
    if (data.success)
      queryClient.setQueryData<ILoungePost[] | undefined>(
        "lounge",
        (postList) =>
          postList?.map((p) => {
            if (p.lounge_id === lounge_id)
              return {
                ...p,
                isfav: data.isfav,
                fav: data.isfav ? p.fav + 1 : p.fav - 1,
              };
            return p;
          })
      );
  };

  const FavButton = () => (
    <Button
      variant="white"
      css={{
        padding: "0px 6px 0px 8px",
        height: "24px",
        gap: "4px",
        alignItems: "center",
        color: "var(--negative2)",
      }}
      onClick={() => handleFav(post)}
    >
      <span css={{ fontSize: "12px" }}>{post.fav}</span>
      {post.isfav || false ? (
        <MdOutlineFavorite style={{ fontSize: "14px", margin: 0 }} />
      ) : (
        <MdOutlineFavoriteBorder style={{ fontSize: "14px", margin: 0 }} />
      )}
    </Button>
  );

  const ProfileImage = () => (
    <span
      css={{
        flex: "0 0 auto",
      }}
    >
      <Image
        css={{
          borderRadius: "24px",
        }}
        width="48px"
        height="48px"
        alt="profile"
        src={post.picture}
      />
    </span>
  );

  return (
    <Flex css={{ gap: "18px", alignItems: "stretch" }}>
      <ProfileImage />
      <Flex column css={{ gap: "12px", flex: "1 1 auto" }}>
        <Flex css={{ justifyContent: "space-between", alignItems: "center" }}>
          <p>
            <b>{post.username}</b> · {timeDiffString(post.date)}
          </p>
          <FavButton />
        </Flex>
        <div css={{ whiteSpace: "pre-line" }}>{post.content}</div>
      </Flex>
    </Flex>
  );
};

const LoungeEditor = () => {
  const styles = {
    TextArea: css({
      background: "transparent",
      fontSize: "18px",
      marginTop: "10px",
      flex: "1 0 auto",
      resize: "none",
      borderWidth: "0px",
      ":focus": {
        outline: "0px",
      },
    }),
    Dialog: css({
      justifyContent: "flex-end",
      alignItems: "center",
      gap: "12px",
      lineHeight: "initial",
    }),
  };

  const auth = useAuth();

  const {
    register,
    handleSubmit,
    watch,
    trigger,
    reset,
    formState: { errors },
  } = useForm<{
    content: string;
  }>({
    mode: "all",
  });

  useEffect(() => {
    trigger();
  }, [trigger]);

  const watchContent = watch("content", "");

  const onSubmit = async (data: any) => {
    const res = await http.post<{ success: boolean }>("/lounge/add", data);
    if (res.data.success) {
      reset();
      refetch();
    } else alert("알 수 없는 오류 발생");
  };

  const { refetch } = useQuery("lounge", async () => {
    return (await http.get("/lounge")).data;
  });

  if (!auth.success) return null;

  const TextArea = () => (
    <ReactTextareaAutosize
      {...register("content", {
        required: true,
        minLength: 10,
        maxLength: 500,
      })}
      placeholder="간단한 이야기를 작성해 보세요..."
      css={styles.TextArea}
      maxLength={500}
      minRows={3}
      spellCheck="false"
      autoComplete="off"
      autoCorrect="off"
      autoCapitalize="off"
    />
  );

  const ProfileImage = () => (
    <div
      css={{
        flex: "0 0 auto",
      }}
    >
      <Image
        css={{
          borderRadius: "24px",
        }}
        width="48px"
        height="48px"
        alt="profile"
        src={auth.profileImage!}
      />
    </div>
  );

  const SubmitButton = () => (
    <Button
      disabled={!!Object.keys(errors).length}
      type="submit"
      variant="primary"
    >
      작성하기
    </Button>
  );

  return (
    <Box responsive column css={{ padding: "16px" }}>
      <form onSubmit={handleSubmit(onSubmit)}>
        <Flex column css={{ gap: "0px" }}>
          <Flex css={{ gap: "18px" }}>
            <ProfileImage />
            <TextArea />
          </Flex>
          <Flex css={styles.Dialog}>
            <span>{watchContent?.length}/500</span>
            <SubmitButton />
          </Flex>
        </Flex>
      </form>
    </Box>
  );
};

const LoungeInfoMessage = () => (
  <Box variant="primary" css={{ lineHeight: "initial" }}>
    로그인 후 라운지에 이야기를 작성해 보세요.
  </Box>
);

const Lounge = () => {
  const auth = useAuth();

  let { data } = useQuery<ILoungePost[]>("lounge", () => fetchLounge());

  return (
    <ChildrenContainer width={840}>
      <SectionHeader>
        <SectionHeader.Title>라운지</SectionHeader.Title>
        <SectionHeader.Description>
          Lorem ipsum dolor sit amet, consectetur adipiscing elit.
        </SectionHeader.Description>
      </SectionHeader>
      <SectionBody>
        <Flex column css={{ gap: "24px" }}>
          {auth.success ? <LoungeEditor /> : <LoungeInfoMessage />}
          <Box responsive column css={{ gap: "16px", padding: "16px" }}>
            {data?.map((post, i) => (
              <Fragment key={post.lounge_id}>
                {!!i && <Hr />}
                <LoungePost post={post} />
              </Fragment>
            ))}
          </Box>
        </Flex>
      </SectionBody>
    </ChildrenContainer>
  );
};

/* -------------------------------------------------------------------------- */
/*                                     api                                    */
/* -------------------------------------------------------------------------- */

// function Query<TQueryFnData = unknown, TQueryKey extends QueryKey = QueryKey>(
//   this: {
//     http: AxiosInstance;
//   },
//   queryKey: TQueryKey,
//   queryFn:
// ) {
//   return (this.http) => {

//   };
// }

// const makeQuery = <
//   TQueryFnData = unknown,
//   TQueryKey extends QueryKey = QueryKey
// >({
//   queryKey,
//   queryFn,
// }: {
//   queryKey: TQueryKey;
//   queryFn: QueryFunction<TQueryFnData, TQueryKey>;
// }) => {
//   return new Query(queryKey, queryFn)
// };

const fetchLounge = async (_http = http) => {
  const res = await _http.get("/lounge");
  return res.data;
};

export const getServerSideProps: GetServerSideProps = injectSession(
  async ({ http }) => {
    const queryClient = new QueryClient();

    const res = await Promise.all([
      queryClient.prefetchQuery("lounge", () => fetchLounge(http)),
    ]);

    return {
      props: {
        dehydratedState: dehydrate(queryClient),
      },
    };
  }
);

export default Lounge;
