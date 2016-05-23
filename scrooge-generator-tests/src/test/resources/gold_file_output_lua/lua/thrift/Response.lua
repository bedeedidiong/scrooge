--[[
  Generated by Scrooge
    version: ?
    rev: ?
    built at: ?
--]]

-- Import this file with:
--   require 'thrift.Response'
-- Note: This file depends on libthrift!

local ResponseUnion = require 'thrift.ResponseUnion'

local Response = {
  ttype = 'struct',
  name = 'Response',
  fields = {
    [1] = { name = 'statusCode', required = true, ttype = 'i32', },
    [2] = { name = 'responseUnion', required = true, ttype = 'struct', fields = ResponseUnion.fields, },
  }
}

local binaryCodec = require 'libthrift'

Response.__codec = binaryCodec.codec(Response)

function Response:read(bin)
  return self.__codec:read(bin)
end

function Response:readTensor(bin)
  return self.__codec:readTensor(bin)
end

function Response:write(params)
  return self.__codec:write(params)
end

function Response:writeTensor(params)
  return self.__codec:writeTensor(params)
end

return Response
