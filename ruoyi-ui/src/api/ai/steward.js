import request from '@/utils/request'

// AI 管家对话
export function chatWithSteward(data) {
  return request({
    url: '/ai/steward/chat',
    method: 'post',
    data: data
  })
}
