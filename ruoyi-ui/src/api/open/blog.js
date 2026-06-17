import request from '@/utils/request'

// 查询公开文章列表
export function listPublicArticle(query) {
  return request({
    url: '/open/blog/article/list',
    method: 'get',
    headers: { isToken: false },
    params: query
  })
}

// 查询公开文章详情
export function getPublicArticle(articleId) {
  return request({
    url: '/open/blog/article/' + articleId,
    method: 'get',
    headers: { isToken: false }
  })
}

// 查询公开分类列表
export function listPublicCategory(query) {
  return request({
    url: '/open/blog/category/list',
    method: 'get',
    headers: { isToken: false },
    params: query
  })
}

// 查询公开标签列表
export function listPublicTag(query) {
  return request({
    url: '/open/blog/tag/list',
    method: 'get',
    headers: { isToken: false },
    params: query
  })
}
