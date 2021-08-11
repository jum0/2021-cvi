const ERROR_MESSAGE = {
  FAIL_TO_GET_REVIEW_LIST: '후기 목록을 불러올 수 없습니다',
  FAIL_TO_GET_REVIEW: '해당 후기를 불러올 수 없습니다',
};

const ALERT_MESSAGE = {
  SUCCESS_TO_SIGNUP: '회원가입에 성공했습니다',
  FAIL_TO_LOGIN: '닉네임에 공백과 특수문자는 포함할 수 업습니다.',
  FAIL_TO_CREATE_REVIEW: '후기를 작성할 수 없습니다.',
  FAIL_TO_EDIT_REVIEW: '후기를 수정할 수 없습니다.',
  FAIL_TO_DELETE_REVIEW: '후기를 삭제할 수 없습니다.',
  FAIL_TO_CREATE_COMMENT: '댓글을 작성할 수 없습니다.',
  FAIL_TO_EDIT_COMMENT: '댓글을 수정할 수 없습니다.',
  FAIL_TO_DELETE_COMMENT: '댓글을 삭제할 수 없습니다.',
  FAIL_TO_SERVER: '오류가 발생했습니다. 다시 한 번 시도해주세요.',
  FAIL_TO_EDIT_ACCOUNT: '내 정보를 수정할 수 없습니다.',
  FAIL_TO_FULFILL_MIN_LENGTH: '최소 1자 이상 입력해야 합니다.',
  FAIL_TO_ACCESS_EDIT_PAGE: '본인이 작성하지 않은 글은 수정할 수 없습니다.',
  FAIL_TO_ACCESS_SIGNUP_PAGE: '회원가입 페이지에 접근할 수 없습니다.',
  FAIL_TO_EDIT_SHOT_VERIFICATION: '인증에 실패했습니다.',
  FAIL_TO_SHOT_VERIFICATION: '잘못된 사진입니다. 사진을 다시 확인해주세요.',
  NEED_LOGIN: '로그인이 필요한 서비스입니다.',
};

const SNACKBAR_MESSAGE = {
  SUCCESS_TO_SIGNUP: '회원가입에 성공했습니다',
  SUCCESS_TO_LOGIN: '로그인에 성공했습니다',
  SUCCESS_TO_LOGOUT: '로그아웃에 성공했습니다',
  SUCCESS_TO_CREATE_REVIEW: '후기를 작성했습니다',
  SUCCESS_TO_EDIT_REVIEW: '후기를 수정했습니다.',
  SUCCESS_TO_DELETE_REVIEW: '후기를 삭제했습니다.',
  SUCCESS_TO_EDIT_ACCOUNT: '내 정보를 수정했습니다.',
  SUCCESS_TO_CREATE_COMMENT: '댓글을 작성했습니다.',
  SUCCESS_TO_EDIT_COMMENT: '댓글을 수정했습니다.',
  SUCCESS_TO_DELETE_COMMENT: '댓글을 삭제했습니다.',
  SUCCESS_TO_EDIT_SHOT_VERIFICATION: '인증되었습니다.',
};

const CONFIRM_MESSAGE = {
  DELETE_REVIEW: '후기를 삭제하시겠습니까?',
  DELETE_COMMENT: '댓글을 삭제하시겠습니까?',
  CANCEL_LIKE: '좋아요를 취소하시겠습니까?',
  GO_BACK: '현재 화면에서 나가시겠습니까? 변경사항이 저장되지 않을 수 있습니다.',
  OFFER_SHOT_VERIFICATION: '접종 인증 후 후기 작성을 하시겠습니까?',
};

const PLACEHOLDER = {
  COMMENT_FORM:
    '저작권 등 다른 사람의 권리를 침해하거나 명예를 훼손하는 게시물은 이용약관 및 관련 법률에 의해 제재를 받을 수 있습니다. 건전한 토론문화와 양질의 댓글 문화를 위해, 타인에게 불쾌감을 주는 욕설 또는 특정 계층/민족, 종교 등을 비하하는 단어들은 표시가 제한됩니다.',
};

export { ERROR_MESSAGE, ALERT_MESSAGE, SNACKBAR_MESSAGE, CONFIRM_MESSAGE, PLACEHOLDER };
