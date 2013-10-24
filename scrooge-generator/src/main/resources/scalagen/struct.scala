{{#public}}
package {{package}}

import com.twitter.scrooge.{
  TFieldBlob, ThriftException, ThriftStruct, ThriftStructCodec3, ThriftUtil}
import org.apache.thrift.protocol._
import org.apache.thrift.transport.{TMemoryBuffer, TTransport}
import java.nio.ByteBuffer
import scala.collection.immutable.{Map => immutable$Map}
import scala.collection.mutable.{
  ArrayBuffer => mutable$ArrayBuffer, Buffer => mutable$Buffer,
  HashMap => mutable$HashMap, HashSet => mutable$HashSet}
import scala.collection.{Map, Set}

{{/public}}
{{docstring}}
object {{StructName}} extends ThriftStructCodec3[{{StructName}}] {
  val Struct = new TStruct("{{StructNameForWire}}")
{{#fields}}
  val {{fieldConst}} = new TField("{{fieldNameForWire}}", TType.{{constType}}, {{id}})
  val {{fieldConst}}Manifest = implicitly[Manifest[{{fieldType}}]]
{{/fields}}

  /**
   * Checks that all required fields are non-null.
   */
  def validate(_item: {{StructName}}) {
{{#fields}}
{{#required}}
{{#nullable}}
    if (_item.{{fieldName}} == null) throw new TProtocolException("Required field {{fieldName}} cannot be null")
{{/nullable}}
{{/required}}
{{/fields}}
  }

  override def encode(_item: {{StructName}}, _oproto: TProtocol) { _item.write(_oproto) }
  override def decode(_iprot: TProtocol): {{StructName}} = Immutable.decode(_iprot)

  def apply(
{{#fields}}
    {{fieldName}}: {{>optionalType}}{{#hasDefaultValue}} = {{defaultFieldValue}}{{/hasDefaultValue}}{{#optional}} = None{{/optional}},
{{/fields}}
    _passthroughFields: immutable$Map[Short, TFieldBlob] = immutable$Map.empty
  ): {{StructName}} = new Immutable(
{{#fields}}
    {{fieldName}},
{{/fields}}
    _passthroughFields
  )

{{#arity0}}
  def unapply(_item: {{StructName}}): Boolean = true
{{/arity0}}
{{#arity1}}
  def unapply(_item: {{StructName}}): Option[{{>optionalType}}] = Some(_item.{{fieldName}})
{{/arity1}}
{{#arityN}}
  def unapply(_item: {{StructName}}): Option[{{product}}] = Some(_item)
{{/arityN}}

  private class Decoder(_iprot: TProtocol) {
    private[this] var _passthroughFields = immutable$Map.newBuilder[Short, TFieldBlob]
{{#fields}}
    private[this] var {{fieldName}}: {{fieldType}} = {{defaultReadValue}}
    private[this] var {{gotName}} = false
{{/fields}}

{{#fields}}
{{#readWriteInfo}}
    def {{readName}}(_field: TField) {
      {{>readField}}
    }
{{/readWriteInfo}}
{{/fields}}

    def read(): {{StructName}} = {
      var _done = false
      _iprot.readStructBegin()
      while (!_done) {
        val _field = _iprot.readFieldBegin()
        if (_field.`type` == TType.STOP) {
          _done = true
        } else {
          _field.id match {
{{#fields}}
            case {{id}} => {{readName}}(_field) // {{fieldName}}
{{/fields}}
            case _ =>
              _passthroughFields += (_field.id -> TFieldBlob.read(_field, _iprot))
          }
          _iprot.readFieldEnd()
        }
      }
      _iprot.readStructEnd()
      result()
    }

    def result(): {{StructName}} = {
{{#fields}}
{{#required}}
      if (!{{gotName}}) throw new TProtocolException("Required field '{{StructNameForWire}}' was not found in serialized data for struct {{StructName}}")
{{/required}}
{{/fields}}
      new Immutable(
{{#fields}}
{{#optional}}
        if ({{gotName}}) Some({{fieldName}}) else None,
{{/optional}}
{{^optional}}
        {{fieldName}},
{{/optional}}
{{/fields}}
        _passthroughFields.result()
      )
    }
  }

  object Immutable extends ThriftStructCodec3[{{StructName}}] {
    override def encode(_item: {{StructName}}, _oproto: TProtocol) { _item.write(_oproto) }
    override def decode(_iprot: TProtocol): {{StructName}} = new Decoder(_iprot).read()
  }

  /**
   * The default read-only implementation of {{StructName}}.  You typically should not need to
   * directly reference this class; instead, use the {{StructName}}.apply method to construct
   * new instances.
   */
  class Immutable(
{{#fields}}
    val {{fieldName}}: {{>optionalType}}{{#hasDefaultValue}} = {{defaultFieldValue}}{{/hasDefaultValue}}{{#optional}} = None{{/optional}},
{{/fields}}
    override val _passthroughFields: immutable$Map[Short, TFieldBlob] = immutable$Map.empty
  ) extends {{StructName}}

{{#withProxy}}
  /**
   * This Proxy trait allows you to extend the {{StructName}} trait with additional state or
   * behavior and implement the read-only methods from {{StructName}} using an underlying
   * instance.
   */
  trait Proxy extends {{StructName}} {
    protected def {{underlyingStructName}}: {{StructName}}
{{#fields}}
    override def {{fieldName}}: {{>optionalType}} = {{underlyingStructName}}.{{fieldName}}
{{/fields}}
    override def _passthroughFields = {{underlyingStructName}}._passthroughFields
  }
{{/withProxy}}
}

trait {{StructName}} extends {{parentType}}
  with {{product}}
  with java.io.Serializable
{
  import {{StructName}}._

{{#fields}}
{{#isEnum}}
  private[this] val {{fieldConst}}I32 = new TField("{{fieldNameForWire}}", TType.I32, {{id}})
{{/isEnum}}
{{/fields}}

{{#fields}}
  def {{fieldName}}: {{>optionalType}}
{{/fields}}

{{#fields}}
  def _{{indexP1}} = {{fieldName}}
{{/fields}}

  def _passthroughFields: immutable$Map[Short, TFieldBlob] = immutable$Map.empty

  /**
   * Gets a field value encoded as a binary blob using TCompactProtocol.  If the specified field
   * is present in the passthrough map, that value is returend.  Otherwise, if the specified field
   * is known and not optional and set to None, then the field is serialized and returned.
   */
  def getFieldBlob(_fieldId: Short): Option[TFieldBlob] = {
    _passthroughFields.get(_fieldId) orElse {
      _fieldId match {
{{#fields}}
        case {{id}} => {{getBlobName}}
{{/fields}}
        case _ => None
      }
    }
  }

{{#fields}}
  private def {{getBlobName}}: Option[TFieldBlob] =
{{#readWriteInfo}}
{{#optional}}
    if ({{fieldName}}.isDefined) {
{{/optional}}
{{^optional}}
{{#nullable}}
    if ({{fieldName}} ne null) {
{{/nullable}}
{{^nullable}}
    if (true) {
{{/nullable}}
{{/optional}}
      Some(
        TFieldBlob.capture({{StructName}}.{{fieldConst}}) { _oprot =>
          val {{valueVariableName}} = {{fieldName}}{{#optional}}.get{{/optional}}
          {{>writeValue}}
        }
      )
    } else {
      None
    }
{{/readWriteInfo}}
{{/fields}}

  /**
   * Collects TCompactProtocol-encoded field values according to `getFieldBlob` into a map.
   */
  def getFieldBlobs(ids: TraversableOnce[Short]): immutable$Map[Short, TFieldBlob] =
    (ids flatMap { id => getFieldBlob(id) map { id -> _ } }).toMap

  /**
   * Sets a field using a TCompactProtocol-encoded binary blob.  If the field is a known
   * field, the blob is decoded and the field is set to the decoded value.  If the field
   * is unknown and passthrough fields are enabled, then the blob will be stored in
   * _passthroughFields.
   */
  def setField(_blob: TFieldBlob): {{StructName}} = {
    _blob.id match {
{{#fields}}
{{#readWriteInfo}}
      case {{id}} => {{setBlobName}}(_blob)
{{/readWriteInfo}}
{{/fields}}
      case _ => copy(_passthroughFields = _passthroughFields + (_blob.id -> _blob))
    }
  }

{{#fields}}
  private def {{setBlobName}}(_blob: TFieldBlob): {{StructName}} = {
{{#readWriteInfo}}
    val {{fieldName}} = {
      val _iprot = _blob.read
      {{>readValue}}
    }
{{#optional}}
    copy({{fieldName}} = Some({{fieldName}}))
{{/optional}}
{{^optional}}
    copy({{fieldName}} = {{fieldName}})
{{/optional}}
{{/readWriteInfo}}
  }
{{/fields}}

  /**
   * If the specified field is optional, it is set to None.  Otherwise, if the field is
   * known, it is reverted to its default value; if the field is unknown, it is subtracked
   * from the passthroughFields map, if present.
   */
  def unsetField(_fieldId: Short): {{StructName}} =
    _fieldId match {
{{#fields}}
      case {{id}} => {{unsetName}}
{{/fields}}
      case _ => copy(_passthroughFields = _passthroughFields - _fieldId)
    }

  /**
   * If the specified field is optional, it is set to None.  Otherwise, if the field is
   * known, it is reverted to its default value; if the field is unknown, it is subtracked
   * from the passthroughFields map, if present.
   */
{{#fields}}
  def {{unsetName}}: {{StructName}} =
{{#optional}}
    copy({{fieldName}} = None, _passthroughFields = _passthroughFields - {{id}})
{{/optional}}
{{^optional}}
    copy({{fieldName}} = {{defaultReadValue}}, _passthroughFields = _passthroughFields - {{id}})
{{/optional}}

{{/fields}}

  override def write(_oprot: TProtocol) {
    {{StructName}}.validate(this)
    _oprot.writeStructBegin(Struct)
{{#fields}}
{{#readWriteInfo}}
    {{>writeField}}
{{/readWriteInfo}}
{{/fields}}
    _passthroughFields.values foreach { _.write(_oprot) }
    _oprot.writeFieldStop()
    _oprot.writeStructEnd()
  }

  def copy(
{{#fields}}
    {{fieldName}}: {{>optionalType}} = this.{{fieldName}},
{{/fields}}
    _passthroughFields: immutable$Map[Short, TFieldBlob] = this._passthroughFields
  ): {{StructName}} =
    new Immutable(
{{#fields}}
      {{fieldName}},
{{/fields}}
      _passthroughFields
    )

  override def canEqual(other: Any): Boolean = other.isInstanceOf[{{StructName}}]

  override def equals(other: Any): Boolean =
    _root_.scala.runtime.ScalaRunTime._equals(this, other) &&
      _passthroughFields == other.asInstanceOf[{{StructName}}]._passthroughFields

  override def hashCode: Int = _root_.scala.runtime.ScalaRunTime._hashCode(this)

  override def toString: String = _root_.scala.runtime.ScalaRunTime._toString(this)

{{#hasExceptionMessage}}
  override def getMessage: String = String.valueOf({{exceptionMessageField}})
{{/hasExceptionMessage}}

  override def productArity: Int = {{arity}}

  override def productElement(n: Int): Any = n match {
{{#fields}}
    case {{index}} => {{fieldName}}
{{/fields}}
    case _ => throw new IndexOutOfBoundsException(n.toString)
  }

  override def productPrefix: String = "{{StructName}}"
}
