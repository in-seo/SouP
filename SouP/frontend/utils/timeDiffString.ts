export function timeDiffString(value: Date | string | number) {
  const today = new Date();
  const timeValue = new Date(value);

  const betweenTime = Math.floor(
    (today.getTime() - timeValue.getTime()) / 1000 / 60
  );
  if (betweenTime < 1) return "방금 전";
  if (betweenTime < 60) {
    return `${betweenTime}분 전`;
  }

  const betweenTimeHour = Math.floor(betweenTime / 60);
  if (betweenTimeHour < 24) {
    return `${betweenTimeHour}시간 전`;
  }

  const betweenTimeDay = Math.floor(betweenTime / 60 / 24);
  if (betweenTimeDay < 7) {
    return `${betweenTimeDay}일 전`;
  }

  const betweenTimeWeek = Math.floor(betweenTime / 60 / 24 / 7);
  if (betweenTimeDay < 30) {
    return `${betweenTimeWeek}주 전`;
  }

  const betweenTimeMonth = Math.floor(betweenTime / 60 / 24 / 30);
  if (betweenTimeDay < 365) {
    return `${betweenTimeMonth}달 전`;
  }

  return `${Math.floor(betweenTimeDay / 365)}년 전`;
}
