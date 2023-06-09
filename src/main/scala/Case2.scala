import io.circe.{Codec, Encoder, Json, Decoder}
import io.circe.syntax.*
import io.circe.parser.{decode, parse}

import scala.quoted.staging
import scala.quoted.{Quotes, Type}

@main def Case2: Unit = {
  val settings =
    staging.Compiler.Settings.make(compilerArgs = List("-Yexplicit-nulls"))
  given explicitNullsCompiler: staging.Compiler =
    staging.Compiler.make(getClass.getClassLoader)(settings)

  case class Foo(val i: Int, val s: String) derives Codec.AsObject

  def expr(using Quotes, Type[Foo]) = '{
    given Codec[Foo] = Codec.AsObject.derived[Foo]
    given Decoder[Foo] = Decoder.derived[Foo]
    given Encoder[Foo] = Encoder.AsObject.derived[Foo]

    println(
      """access to object Foo from wrong staging level:
        |  - the definition is at level 0,
        |  - but the access is at level 1.
        """.stripMargin
    )
  }

  staging.run(expr)
}
