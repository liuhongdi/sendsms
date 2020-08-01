local key = KEYS[1]
local keyseconds = tonumber(KEYS[2])
local daycount = tonumber(KEYS[3])
local keymobile = 'SmsAuthKey:'..key
local keycount = 'SmsAuthCount:'..key
--redis.log(redis.LOG_NOTICE,' keyseconds: '..keyseconds..';daycount:'..daycount)
local current = redis.call('GET', keymobile)
--redis.log(redis.LOG_NOTICE,' current: keymobile:'..current)
if current == false then
   --redis.log(redis.LOG_NOTICE,keymobile..' is nil ')
   local count = redis.call('GET', keycount)
   if count == false then
      redis.call('SET', keycount,1)
      redis.call('EXPIRE',keycount,86400)

      redis.call('SET', keymobile,1)
      redis.call('EXPIRE',keymobile,keyseconds)
      return '1'
   else
      local num_count = tonumber(count)
      if num_count+1 > daycount then
         return '2'
      else
         redis.call('INCRBY',keycount,1)

         redis.call('SET', keymobile,1)
         redis.call('EXPIRE',keymobile,keyseconds)
         return '1'
      end
   end
else
   --redis.log(redis.LOG_NOTICE,keymobile..' is not nil ')
   return '0'
end